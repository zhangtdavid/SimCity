package city.roles;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import city.Application;
import city.Role;
import city.buildings.BankBuilding;
import city.buildings.BankBuilding.Account;
import city.buildings.BankBuilding.Loan;
import city.interfaces.BankManager;

public class BankManagerRole extends Role implements BankManager{
// Data
	BankBuilding building;
	List<MyTeller> myTellers = new ArrayList<MyTeller>();
	List<BankCustomerRole> customers = new ArrayList<BankCustomerRole>();
	List<BankTask> bankTasks = new ArrayList<BankTask>();
	Role directDepositer = null;
	private static final int loanInterval = 50;
// Constructor
	public BankManagerRole (BankBuilding b){
		building = b;
	}
// Messages
	//from customer
	public void msgNeedService(BankCustomerRole bc){
		customers.add(bc);
		stateChanged();
	}
	public void msgDirectDeposit(int acctNum, double money, Role r){
		bankTasks.add(new BankTask(acctNum, type.deposit, money, null));
		directDepositer = r;
		stateChanged();
	}
	//from teller
	public void msgAvailable(BankTellerRole t){
		for(MyTeller myT : myTellers){
			if(myT.teller == t)
				myT.s = state.available;
			else
				myTellers.add(new MyTeller(t));
		}
		stateChanged();
	}
	public void msgWithdraw(int acctNum, double money, BankTellerRole t){
		bankTasks.add(new BankTask(acctNum, type.withdrawal, money, t));
		stateChanged();
	}
	public void msgCreateAccount(double money, BankTellerRole t){
		bankTasks.add(new BankTask(0, type.acctCreate, money, t));
		stateChanged();
	}
	public void msgCreateLoan(double amt, double monthly, int acct){
		building.loans.add(new Loan(amt, monthly, acct));
		stateChanged();
	}
// Scheduler
	@Override
	public boolean runScheduler() {
		if(LoanPaymentDue()){
			for(Loan l : building.loans){
				if(l.remaining > 0){
					PayLoan(l);
				}
				else{
					building.loans.remove(l);
				}
			}
		}
		for(BankCustomerRole bc : customers){
			for(MyTeller myT : myTellers){
				if(myT.s == state.available){
					AssignCustomer(bc, myT);
					return true;
				}
			}
		}	
		for(BankTask bT : bankTasks){
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
			}	
			if(bT.t == type.acctCreate){
				CreateAccount(bT);
				return true;
			}
		}
		// TODO Auto-generated method stub
		return false;
	}
	
// Actions
	private boolean LoanPaymentDue() {
		Calendar c = Calendar.getInstance();
		c.setTime(this.getPerson().getDate());
		int day = c.get(Calendar.DAY_OF_YEAR);
		c.setTime(LoanPayDate());
		int due = c.get(Calendar.DAY_OF_YEAR);
		return (day == due);
	}
	
	private Date LoanPayDate() {
		long interval = (Application.INTERVAL * loanInterval);	
		Date dueDate = new Date(0);
		dueDate.setTime(building.loanLastPaid.getTime() + interval);
		return dueDate;
	}
	
	private void AssignCustomer(BankCustomerRole bc, MyTeller myT){
		myT.s = state.busy;
		customers.remove(bc);
		myT.teller.msgAddressCustomer(bc);
	}
	private void Deposit(BankTask bT){
		for(Account a : building.accounts){
			if(a.acctNum == bT.acctNum){
				a.balance += bT.money;
				bankTasks.remove(bT);
				//directDepositer.msgDepositSuccessful();
				directDepositer = null;
			}
		}
	}
	private void Withdraw(BankTask bT){
		List<Account> temp = building.getAccounts();
		for(Account a : temp){
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
		bT.teller.msgTransactionSuccessful();
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
		BankTellerRole teller;
		double salary;
		state s;
		public MyTeller(BankTellerRole t){
			teller = t;
			salary = 100000000;
			s = state.available;
		}
	}
	public class BankTask {
		int acctNum;
		type t;
		double money;
		BankTellerRole teller;
		BankCustomerRole bc;
		public BankTask(int acct, type typ, double m, BankTellerRole tell){
			acctNum = acct;
			t = typ;
			money = m;
			teller = tell;
		}
	}
	public enum state {available, busy, gone};
	public enum type {deposit, directDeposit, withdrawal, loanPayment, acctCreate};
}
