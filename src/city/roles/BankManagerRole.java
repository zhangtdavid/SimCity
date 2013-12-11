package city.roles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import trace.AlertLog;
import trace.AlertTag;
import city.animations.BankManagerAnimation;
import city.bases.JobRole;
import city.buildings.BankBuilding;
import city.buildings.BankBuilding.Account;
import city.buildings.BankBuilding.Loan;
import city.buildings.interfaces.Bank;
import city.roles.interfaces.BankCustomer;
import city.roles.interfaces.BankManager;
import city.roles.interfaces.BankTeller;

public class BankManagerRole extends JobRole implements BankManager {
	
	// Data

	public BankBuilding building;
	public List<MyTeller> myTellers = new ArrayList<MyTeller>();
	public List<BankCustomer> customers = Collections.synchronizedList(new ArrayList<BankCustomer>());
	public List<BankTask> bankTasks = new ArrayList<BankTask>();
	private boolean wantsInactive = false;
	private Semaphore atDestination = new Semaphore(0,true);
	private BankManagerAnimation gui = null;
	
	// Constructor

	public BankManagerRole (Bank b, int shiftStart, int shiftEnd){
		building = (BankBuilding) b;
		this.setWorkplace(b);
		this.setSalary(Bank.WORKER_SALARY);
		this.setShift(shiftStart, shiftEnd);
	}

	// Messages

	@Override
	public void msgNeedService(BankCustomer bc){
		print("Need service message received");
		customers.add(bc);
		stateChanged();
	}
	
	@Override
	public void msgDirectDeposit(int acctNum, int money, BankCustomer r){
		print("Direct Deposit message received");
		if(acctNum == -1)
			bankTasks.add(new BankTask(acctNum, TYPE.acctCreate, money, null, r));
		else
			bankTasks.add(new BankTask(acctNum, TYPE.deposit, money, null, r));
		stateChanged();
	}
	
	// from teller
	
	@Override
	public void msgAvailable(BankTeller t){
		boolean flag = true;
		//print("Available message received from " + t.getPerson().getName() + ". Number of tellers = " + myTellers.size());
		for(MyTeller myT : myTellers){
			if(myT.teller == t){
				myT.s = STATE.available;
				flag = false;
				stateChanged();
			}
		}
		if(flag){
		myTellers.add(new MyTeller(t));
		stateChanged();
		}
	}
	
	@Override
	public void msgUnavailable(BankTeller t){
		print("Unavailable message received");
		for(MyTeller myT : myTellers){
			if(myT.teller == t){
				myT.s = STATE.gone;
				stateChanged();
			}
		}
	}
	
	@Override
	public void msgWithdraw(int acctNum, int money, BankTeller t){
		print("Withdraw message received from Teller");
		bankTasks.add(new BankTask(acctNum, TYPE.withdrawal, money, t, null));
		stateChanged();
	}
	
	@Override
	public void msgTryDeposit(int money, int acctNum, BankTeller t){
		print("Try deposit message received from teller");
		if(acctNum == -1)
			bankTasks.add(new BankTask(acctNum, TYPE.acctCreate, money, t, null));
		else
			bankTasks.add(new BankTask(acctNum, TYPE.deposit, money, t, null));
		stateChanged();
	}
	
	@Override
	public void msgCreateLoan(int amt, int monthly, int acct){
		print("Create Loan message received from Teller");
		building.loans.add(new Loan(amt, monthly, acct));
		stateChanged();
	}

	// Scheduler

	@Override
	public boolean runScheduler() {
		
		if(wantsInactive && building.getManager() != this && customers.size() == 0){
			super.setInactive();
			wantsInactive = false;
			this.getPerson().setCash(this.getPerson().getCash() + Bank.WORKER_SALARY);
			return false;
		}
		for(BankCustomer bc : customers){
			for(MyTeller myT : myTellers){
				if(myT.s == STATE.available){
					AssignCustomer(bc, myT);
					return true;
				}
			}
		}	
		for(BankTask bT : bankTasks){
				if(bT.t == TYPE.deposit){
					Deposit(bT);
					return true;
				}
				if(bT.t == TYPE.withdrawal){
					for(Account a : building.accounts){
						if(a.acctNum == bT.acctNum){
							if(a.balance >= bT.money){
								Withdraw(bT);
								return true;
							} else {
								if(bT.bc == null){
									WithdrawalFailed(bT);
									return true;
								}
								if(bT.bc != null || bT.bc.getBusiness() != null){
									bT.money = a.balance; //If not enough desired money, get all you can instead. ~RCHOI EDIT
									Withdraw(bT);
									return true;
								}else{
									WithdrawalFailed(bT);
									return true;
								}
							}
						}
					}
				}	
				if(bT.t == TYPE.acctCreate){
					CreateAccount(bT);
					return true;
				}
			}
		return false;
	}
	
	// Actions

	private void AssignCustomer(BankCustomer bc, MyTeller myT){
		if(gui!=null)
			gui.DoString("Go to " + myT.teller.getPerson().getName());
		myT.s = STATE.busy;
		customers.remove(bc);
		myT.teller.msgAddressCustomer(bc);
	}
	
	private void Deposit(BankTask bT){
		print("Deposit");
		for(Account a : building.accounts){
			if(a.acctNum == bT.acctNum){		
				a.balance += bT.money;
				bankTasks.remove(bT);
				if(bT.teller != null)
					bT.teller.msgTransactionSuccessful();
				else
					bT.bc.msgDepositCompleted();
			}
		}
		Loan temp = null;
		for(Loan l : building.loans){
			if(l.acctNum == bT.acctNum){
				if (l.remaining > 0) 
					PayLoan(l);
				else 
					temp = l;
			}
		}
		building.loans.remove(temp);
	}
	
	private void Withdraw(BankTask bT){
		for(Account a : building.getAccounts()){
			if(a.acctNum == bT.acctNum){
				a.balance -= bT.money;
				bankTasks.remove(bT);
				bT.teller.msgTransactionSuccessful();
			}
		}
	}
	
	private void WithdrawalFailed(BankTask bT){
		bankTasks.remove(bT);
		bT.teller.msgWithdrawalFailed();
	}
	
	private void CreateAccount(BankTask bT){
		building.accounts.add(new Account(building.accounts.size() + 1, bT.money));
		bankTasks.remove(bT);
		if(bT.bc == null)
			bT.teller.msgHereIsAccount(building.accounts.size());
		else {
			bT.bc.msgAccountCreated(building.accounts.size());
		}
	}
	
	private void PayLoan(Loan l){
		for(Account a : building.getAccounts()){
			if(a.acctNum == l.acctNum){
				a.balance -= l.monthlyPayment;
				l.remaining -= l.monthlyPayment;
			}
		}
	}

	// Getters
	
	@Override
	public List<BankTask> getBankTasks() {
		return bankTasks;
	}
	
	// Setters
	
	@Override
	public void setActive() {
		building.setManager(this);
		super.setActive();
		if(gui!=null)
			gui.DoEnterBank();
	}
	
	public void setInactive(){
		if(building.getManager() != this && customers.size() == 0){
			super.setInactive();
			this.getPerson().setCash(this.getPerson().getCash() + Bank.WORKER_SALARY);
		}
		else{
			wantsInactive = true;
			stateChanged();
		}
	}
	
	// Utilities
	
	@Override
	public void print(String msg) {
		this.getPerson().printViaRole("BankManager", msg);
        AlertLog.getInstance().logMessage(AlertTag.BANK, "BankManagerRole " + this.getPerson().getName(), msg);
    }
	
	// Classes

	public class MyTeller {
		BankTeller teller;
		int salary;
		STATE s;
		public MyTeller(BankTeller t){
			teller = t;
			salary = 100000000;
			s = STATE.available;
		}
	}
	
	public class BankTask {
		int acctNum;
		TYPE t;
		int money;
		BankTeller teller;
		BankCustomer bc;
		public BankTask(int acct, TYPE typ, int m, BankTeller tell, BankCustomer r){
			acctNum = acct;
			t = typ;
			money = m;
			teller = tell;
			bc = r;
		}
	}

	public void msgAtDestination() {
		atDestination.release();// = true;
		stateChanged();
		// TODO Auto-generated method stub
		
	}

	public void setGui(BankManagerAnimation anim) {
		gui = anim;
		// TODO Auto-generated method stub
		
	}
}
