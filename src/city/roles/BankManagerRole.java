package city.roles;

import java.util.ArrayList;
import java.util.List;

import trace.AlertLog;
import trace.AlertTag;
import city.Role;
import city.buildings.BankBuilding;
import city.buildings.BankBuilding.Account;
import city.buildings.BankBuilding.Loan;
import city.interfaces.Bank;
import city.interfaces.BankCustomer;
import city.interfaces.BankManager;
import city.interfaces.BankTeller;

public class BankManagerRole extends Role implements BankManager {
	
	// Data

	private BankBuilding building;
	private List<MyTeller> myTellers = new ArrayList<MyTeller>();
	private List<BankCustomer> customers = new ArrayList<BankCustomer>();
	private List<BankTask> bankTasks = new ArrayList<BankTask>();
	private boolean wantsInactive = false;
	
	// Constructor

	public BankManagerRole (BankBuilding b, int shiftStart, int shiftEnd){
		building = b;
		this.setWorkplace(b);
		this.setSalary(Bank.WORKER_SALARY);
		this.setShift(shiftStart, shiftEnd);
		setActivityBegun();
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
		print("Available message received");
		for(MyTeller myT : myTellers){
			if(myT.teller == t){
				myT.s = STATE.available;
				stateChanged();
			}
		}
		myTellers.add(new MyTeller(t));
		stateChanged();
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
		myTellers.add(new MyTeller(t));
		stateChanged();
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
		print("IN THE SCHEDULER OF BANK MANAGER");
		
		if(wantsInactive && building.manager != this && customers.size() == 0){
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
				if(bT.t == TYPE.atmDeposit){
					atmDeposit(bT);
					return true;
				}
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
								WithdrawalFailed(bT);
								return true;
							}
						}
					}
					print("account not found");
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
		myT.s = STATE.busy;
		customers.remove(bc);
		myT.teller.msgAddressCustomer(bc);
	}

	private void atmDeposit(BankTask bT){
		print("ATM Deposit");
		for(Account a : building.accounts){
			if(a.acctNum == bT.acctNum){
				print("Balance before deposit: " + bT.money);
				a.balance += bT.money;
				print("Balance after deposit: " + bT.money);
				bankTasks.remove(bT);
				bT.bc.msgDepositCompleted();
				return;
			}
		}
	}
	
	private void Deposit(BankTask bT){
		print("Deposit");
		for(Account a : building.getAccounts()){
			if(a.acctNum == bT.acctNum){		
				a.balance += bT.money;
				bankTasks.remove(bT);
				bT.teller.msgTransactionSuccessful();
			}
		}
		for(Loan l : building.loans){
			if(l.acctNum == bT.acctNum){
				if (l.remaining > 0) 
					PayLoan(l);
				else 
					building.loans.remove(l);
			}
		}
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
	public void setInactive(){
		if(building.manager != this && customers.size() == 0){
			super.setInactive();
			this.getPerson().setCash(this.getPerson().getCash() + Bank.WORKER_SALARY);
		}
		else
			wantsInactive = true;
	}
	
	// Utilities
	
	@Override
	public void print(String msg) {
        super.print(msg);
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
}
