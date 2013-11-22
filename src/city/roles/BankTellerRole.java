package city.roles;

import city.Role;
import city.interfaces.BankTeller;

public class BankTellerRole extends Role implements BankTeller {
	
// Data
	//TellerGui gui;
	BankManagerRole b;
	int boothNumber;
	MyCustomer currentCustomer;
	// Constructor
	
// Messages
	//From BankManager
	public void msgAddressCustomer(BankCustomerRole bc){
		print("Address Customer msg received");
		currentCustomer = new MyCustomer(bc);
		currentCustomer.s = serviceState.needsService;
		stateChanged();
	}
	public void msgHereIsAccount(int acctNum){
		print("Here is Account msg received");
		if(currentCustomer.t == serviceType.acctCreate){
			currentCustomer.acctNum = acctNum;
			currentCustomer.s = serviceState.confirmed;
			stateChanged();
		}
	}
	public void msgWithdrawalFailed(){
		print("Withdrawal failed msg received");
		if(currentCustomer.t == serviceType.withdrawal){
			currentCustomer.s = serviceState.failed;
			stateChanged();
		}
	}
	public void msgTransactionSuccessful(){
		print("Transaction successful msg received");
		currentCustomer.s = serviceState.finished;		//confirmed
		stateChanged();
	}
	//From BankCustomer
	public void msgWithdraw(int acctNum, int money, int salary){
		print("Withdraw message received from Customer");
		currentCustomer.s = serviceState.pending;
		currentCustomer.t = serviceType.withdrawal;
		currentCustomer.acctNum = acctNum;
		currentCustomer.amount = money;
		currentCustomer.salary = salary;
	}
	public void msgCreateAccount(int money){
		print("Create account message received");
		currentCustomer.s = serviceState.pending;
		currentCustomer.t = serviceType.acctCreate;
		currentCustomer.amount = money;
	}
	public void msgDoneAndLeaving() {
		print("Done and Leaving message received");
		currentCustomer.s = serviceState.done;
		stateChanged();
	}
// Scheduler
	@Override
	public boolean runScheduler() {
		// TODO Auto-generated method stub
		if(currentCustomer != null){
			if(currentCustomer.s == serviceState.needsService){
				ServiceCustomer();
				return true;
			}
			if(currentCustomer.t == serviceType.acctCreate){
				if(currentCustomer.s == serviceState.pending){
					CreateAccount();
					return true;
				}
				if(currentCustomer.s == serviceState.confirmed){
					GiveAccountNumber();
					return true;
				}
			}
			if(currentCustomer.t == serviceType.withdrawal){
				if(currentCustomer.s == serviceState.pending){
					TryWithdraw();
					return true;
				}
				if(currentCustomer.s == serviceState.confirmed){
					TransferWithdrawal();
					return true;
				}
				if(currentCustomer.s == serviceState.failed){
					CheckLoanEligible();
					return true;
				}
			}
			if(currentCustomer.s == serviceState.finished){	//confirmed?
				FinishInteraction();
				return true;
			}
			if(currentCustomer.s == serviceState.done){
				GetNewCustomer();
			}
		}
		return false;
	}
	
// Actions
	public void ServiceCustomer(){
		currentCustomer.bc.msgWhatDoYouWant(boothNumber, this);
		currentCustomer.s = serviceState.inProgress;
	}
	public void CreateAccount(){
		currentCustomer.s = serviceState.inProgress;
		b.msgCreateAccount(currentCustomer.amount, this);
	}
	public void GiveAccountNumber(){
		currentCustomer.bc.msgAccountCreated(currentCustomer.acctNum);
		currentCustomer.s = serviceState.finished;
	}
	public void TryWithdraw(){
		b.msgWithdraw(currentCustomer.acctNum, currentCustomer.amount, this);
		currentCustomer.s = serviceState.inProgress;
	}
	public void TransferWithdrawal(){
		currentCustomer.bc.msgHereIsWithdrawal(currentCustomer.amount);
		currentCustomer.s = serviceState.finished;
	}
	public void CheckLoanEligible(){
		currentCustomer.s = serviceState.inProgress;
		if(currentCustomer.salary == 0){
			currentCustomer.bc.msgTransactionDenied();
			//Non- norm and will hang for now
		}
		else if(currentCustomer.salary*2 > currentCustomer.amount){
			b.msgCreateLoan(currentCustomer.amount, currentCustomer.amount/4, currentCustomer.acctNum);
			currentCustomer.bc.msgLoanGranted(currentCustomer.amount);
			currentCustomer.s = serviceState.finished;
		}
	}
	public void FinishInteraction(){
		currentCustomer.s = serviceState.inProgress;
		currentCustomer.bc.msgTransactionCompleted();
	}
	
	public void GetNewCustomer(){
		currentCustomer = null;
		b.msgAvailable(this);
	}
	// Getters
	
	// Setters
	
	// Utilities

	// Classes
	public class MyCustomer{
		int acctNum;
		BankCustomerRole bc;
		int amount;
		int salary;
		serviceState s;
		serviceType t;
		public MyCustomer(BankCustomerRole r){
			bc = r;
		}
	}
}
