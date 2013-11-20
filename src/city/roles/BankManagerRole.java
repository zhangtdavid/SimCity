package city.roles;

import java.util.ArrayList;
import java.util.List;

import city.Role;
import city.interfaces.BankManager;

public class BankManagerRole extends Role implements BankManager{
// Data
	List<Loan> loans = new ArrayList<Loan>();
	List<Account> accounts = new ArrayList<Account>();
	List<MyTeller> myTellers = new ArrayList<MyTeller>();
	List<BankCustomerRole> customers = new ArrayList<BankCustomerRole>();
	List<BankTask> bankTasks = new ArrayList<BankTask>();
	Role directDepositer = null;
	Double funds;	
	// Constructor
					// DO THIS!!!!!!!!!!
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
		loans.add(new Loan(amt, monthly, acct));
		stateChanged();
	}
// Scheduler
	@Override
	public boolean runScheduler() {
		for(BankCustomerRole bc : customers){
			for(MyTeller myT : myTellers){
				if(myT.s == state.available){
					AssignCustomer(bc, myT);
					return true;
				}
			}
			for(BankTask bT : bankTasks){
				if(bT.t == type.deposit){
					Deposit(bT);
					return true;
				}
				if(bT.t == type.withdrawal){
					for(Account a : accounts){
						if(a.acctNum == bT.acctNum)
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
				if(bT.t == type.acctCreate){
					CreateAccount(bT);
					return true;
				}
			}
			//PUT IN LOAN PAYMENT RULES CORRESPONDING TO GLOBAL CLOCK
		}
		// TODO Auto-generated method stub
		return false;
	}
	
// Actions
	private void AssignCustomer(BankCustomerRole bc, MyTeller myT){
		myT.teller.msgAddressCustomer(bc);
		customers.remove(bc);
	}
	private void Deposit(BankTask bT){
		for(Account a : accounts){
			if(a.acctNum == bT.acctNum){
				a.balance += bT.money;
				bankTasks.remove(bT);
				//directDepositer.msgDepositSuccessful();
				directDepositer = null;
			}
		}
	}
	private void Withdraw(BankTask bT){
		for(Account a : accounts){
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
		accounts.add(new Account(accounts.size() + 1, bT.money));
		bankTasks.remove(bT);
		bT.teller.msgTransactionSuccessful();
	}
	private void PayLoan(Loan l){
		for(Account a : accounts){
			if(a.acctNum == l.acctNum){
				a.balance -= l.monthlyPayment;
				l.remaining -= l.monthlyPayment;
			}
		}
	}
// Getters
	
// Setters
	
// Utilities
	
// Classes
	class Loan {
		double monthlyPayment;
		double remaining;
		int acctNum;
		public Loan(double amt, double monthly, int acct){
			monthlyPayment = monthly;
			remaining = amt;
			acctNum = acct;
		}
	}
	class Account {
		int acctNum;
		double balance;
		public Account(int num, double money){
			balance = money;
			acctNum = num;
		}
	}
	class MyTeller {
		BankTellerRole teller;
		double salary;
		state s;
		public MyTeller(BankTellerRole t){
			teller = t;
			salary = 100000000;
			s = state.available;
		}
	}
	class BankTask {
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
	enum state {available, busy, gone};
	enum type {deposit, directDeposit, withdrawal, loanPayment, acctCreate};
}
