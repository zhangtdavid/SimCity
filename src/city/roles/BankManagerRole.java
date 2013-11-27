package city.roles;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import city.Application;
import city.Role;
import city.buildings.BankBuilding;
import city.buildings.RestaurantJPBuilding;
import city.buildings.BankBuilding.Account;
import city.buildings.BankBuilding.Loan;
import city.interfaces.BankCustomer;
import city.interfaces.BankManager;
import city.interfaces.BankTeller;

public class BankManagerRole extends Role implements BankManager{
// Data
	BankBuilding building;
	List<MyTeller> myTellers = new ArrayList<MyTeller>();
	List<BankCustomer> customers = new ArrayList<BankCustomer>();
	public List<BankTask> bankTasks = new ArrayList<BankTask>();
	private static final int loanInterval = 50;
	private boolean wantsInactive = false;
	
// Constructor
	public BankManagerRole (BankBuilding b, int shiftStart, int shiftEnd){
		building = b;
		this.setWorkplace(b);
		this.setSalary(RestaurantJPBuilding.WORKER_SALARY);
		this.setShift(shiftStart, shiftEnd);
		setActivityBegun();
	}
	public void setInactive(){
		if(building.manager != this && customers.size() == 0){
			super.setInactive();
		}
		else
			wantsInactive = true;
	}
// Messages
	//from customer
	public void msgNeedService(BankCustomer bc){
		print("Need service message received");
		customers.add(bc);
		stateChanged();
	}
	public void msgDirectDeposit(int acctNum, int money, BankCustomer r){
		print("Direct Deposit message received");
		bankTasks.add(new BankTask(acctNum, type.atmDeposit, money, null, r));
		if(acctNum == -1)
			bankTasks.add(new BankTask(acctNum, type.acctCreate, money, null, r));
		else
			bankTasks.add(new BankTask(acctNum, type.deposit, money, null, r));
		stateChanged();
		runScheduler();
	}
	//from teller
	public void msgAvailable(BankTeller t){
		print("Available message received");
		for(MyTeller myT : myTellers){
			if(myT.teller == t){
				myT.s = state.available;
				stateChanged();
			}
		}
		myTellers.add(new MyTeller(t));
		stateChanged();
	}
	public void msgUnavailable(BankTeller t){
		print("Unavailable message received");
		for(MyTeller myT : myTellers){
			if(myT.teller == t){
				myT.s = state.gone;
				stateChanged();
			}
		}
		myTellers.add(new MyTeller(t));
		stateChanged();
	}
	public void msgWithdraw(int acctNum, int money, BankTeller t){
		print("Withdraw message received from Teller");
		bankTasks.add(new BankTask(acctNum, type.withdrawal, money, t, null));
		stateChanged();
	}
	public void msgTryDeposit(int money, int acctNum, BankTeller t){
		print("Try deposit message received from teller");
		if(acctNum == -1)
			bankTasks.add(new BankTask(acctNum, type.acctCreate, money, t, null));
		else
			bankTasks.add(new BankTask(acctNum, type.deposit, money, t, null));
		stateChanged();
	}
	public void msgCreateLoan(int amt, int monthly, int acct){
		print("Create Loan message received from Teller");
		building.loans.add(new Loan(amt, monthly, acct));
		stateChanged();
	}
// Scheduler
	@Override
	public boolean runScheduler() {
		System.out.println("IN THE SCHEDULER OF BANK MANAGER");
		if(wantsInactive && building.manager != this && customers.size() == 0){
			super.setInactive();
			wantsInactive = false;
		}
		if(LoanPaymentDue()){
			for(Loan l : building.loans){
				if(l.remaining > 0){
					PayLoan(l);
					return true;
				}
				else{
					building.loans.remove(l);
					return true;
				}
			}
		}
		else {
		for(BankCustomer bc : customers){
			for(MyTeller myT : myTellers){
				if(myT.s == state.available){
					AssignCustomer(bc, myT);
					return true;
				}
			}
		}	
		for(BankTask bT : bankTasks){
			if(bT.t == type.atmDeposit){
				atmDeposit(bT);
				return true;
			}
			if(bT.t == type.deposit){
				Deposit(bT);
				return true;
			}
			if(bT.t == type.withdrawal){
				for(Account a : building.accounts){
					if(a.acctNum == bT.acctNum){
						if(a.balance >= bT.money){
								Withdraw(bT);
								return true;
							}
						else{
							WithdrawalFailed(bT);
							return true;
						}
					}
				}
				print("account not found");
			}	
			if(bT.t == type.acctCreate){
				CreateAccount(bT);
				return true;
			}
		}
		// TODO Auto-generated method stub
		}
		return false;
	}
	
// Actions
	private boolean LoanPaymentDue() {
		return false;
		/*Calendar c = Calendar.getInstance();
		c.setTime(this.getPerson().getDate());
		int day = c.get(Calendar.DAY_OF_YEAR);
		c.setTime(LoanPayDate());
		int due = c.get(Calendar.DAY_OF_YEAR);
		return (day == due);*/	
	}
	
	private Date LoanPayDate() {
		long interval = (Application.INTERVAL * loanInterval);	
		Date dueDate = new Date(0);
		dueDate.setTime(building.loanLastPaid.getTime() + interval);
		return dueDate;
	}
	
	private void AssignCustomer(BankCustomer bc, MyTeller myT){
		myT.s = state.busy;
		customers.remove(bc);
		myT.teller.msgAddressCustomer(bc);
	}
	private void atmDeposit(BankTask bT){
		if(bT.acctNum == -1){
			bankTasks.remove(bT);
			CreateAccount(bT);
		}	
		else{
		for(Account a : building.accounts){
			if(a.acctNum == bT.acctNum){
				a.balance += bT.money;
				bankTasks.remove(bT);
				bT.bc.msgDepositCompleted();
				return;
			}
		}
		}
	}
	private void Deposit(BankTask bT){
		for(Account a : building.getAccounts()){
			if(a.acctNum == bT.acctNum){		
				a.balance += bT.money;
				bankTasks.remove(bT);
				bT.teller.msgTransactionSuccessful();
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
		if(bT.t == type.acctCreate)
			bT.teller.msgHereIsAccount(building.accounts.size());
		else if(bT.t == type.atmDeposit){
			bT.bc.msgAccountCreated(building.accounts.size());
		}
	}
	private void PayLoan(Loan l){
		List<Account> temp = building.getAccounts();
		for(Account a : temp){
			if(a.acctNum == l.acctNum){
				a.balance -= l.monthlyPayment;
				l.remaining -= l.monthlyPayment;
			}
		}
		building.loanLastPaid.setTime(building.loanLastPaid.getTime() + loanInterval);
	}
// Getters
	
// Setters
	
// Utilities
	
// Classes
	public class MyTeller {
		BankTeller teller;
		int salary;
		state s;
		public MyTeller(BankTeller t){
			teller = t;
			salary = 100000000;
			s = state.available;
		}
	}
	public class BankTask {
		int acctNum;
		type t;
		int money;
		BankTeller teller;
		BankCustomer bc;
		public BankTask(int acct, type typ, int m, BankTeller tell, BankCustomer r){
			acctNum = acct;
			t = typ;
			money = m;
			teller = tell;
			bc = r;
		}
	}
}
