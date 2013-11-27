package city.roles;

import trace.AlertLog;
import trace.AlertTag;
import city.Application;
import city.Role;
import city.buildings.BankBuilding;
import city.buildings.RestaurantJPBuilding;
import city.interfaces.BankCustomer;
import city.interfaces.BankTeller;

public class BankTellerRole extends Role implements BankTeller {
	
// Data
	//TellerGui gui;
	BankBuilding building;
	int boothNumber;
	private boolean wantsInactive = false;
	public MyCustomer currentCustomer;
// Constructor
	public BankTellerRole (BankBuilding b, int shiftStart, int shiftEnd){
		building = b;
		this.setWorkplace(b);
		this.setSalary(RestaurantJPBuilding.WORKER_SALARY);
		this.setShift(shiftStart, shiftEnd);
	}
	public void setActive(Application.BANK_SERVICE s, int money, Application.TRANSACTION_TYPE t){
		print("Customer has been set active");
		building.manager.msgAvailable(this);
		this.setActivityBegun();
	}
	public void setInactive(){
		if(currentCustomer == null)
			super.setInactive();
		else{
			building.manager.msgUnavailable(this);
			wantsInactive = true;
		}
	}
// Messages
	//From BankManager
	public void msgAddressCustomer(BankCustomer bc){
		print("Address Customer msg received");
		currentCustomer = new MyCustomer(bc);
		currentCustomer.s = serviceState.needsService;
		stateChanged();
	}
	public void msgHereIsAccount(int acctNum){
		print("Here is Account msg received");
		if(currentCustomer.t == serviceType.deposit){
			currentCustomer.acctNum = acctNum;
			currentCustomer.s = serviceState.newAccount;
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
		if(currentCustomer.t == serviceType.deposit)
			currentCustomer.s = serviceState.confirmed;
		else if(currentCustomer.t == serviceType.withdrawal)
			currentCustomer.s = serviceState.confirmed;
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
		stateChanged();
	}
	public void msgDeposit(int money, int acctNum){
		print("Deposit message received");
		currentCustomer.s = serviceState.pending;
		currentCustomer.t = serviceType.deposit;
		currentCustomer.amount = money;
		currentCustomer.acctNum = acctNum;
		stateChanged();
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
		if(wantsInactive && currentCustomer == null){
			super.setInactive();
			wantsInactive = false;
		}
		if(currentCustomer != null){
			if(currentCustomer.s == serviceState.needsService){
				ServiceCustomer();
				return true;
			}
			if(currentCustomer.t == serviceType.deposit){
				if(currentCustomer.s == serviceState.pending){
					TryDeposit();
					return true;
				}
				if(currentCustomer.s == serviceState.newAccount){
					GiveAccountNumber();
					return true;
				}
				if(currentCustomer.s == serviceState.confirmed){
					DepositSuccessful();
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

			if(currentCustomer.s == serviceState.done){
				GetNewCustomer();
				return true;
			}
		}
		return false;
	}
	
// Actions
	public void ServiceCustomer(){
		currentCustomer.bc.msgWhatDoYouWant(boothNumber, this);
		currentCustomer.s = serviceState.inProgress;
	}
	public void TryDeposit(){
		currentCustomer.s = serviceState.inProgress;
		building.getManager().msgTryDeposit(currentCustomer.amount, currentCustomer.acctNum, this);
	}
	public void GiveAccountNumber(){
		currentCustomer.bc.msgAccountCreated(currentCustomer.acctNum);
		currentCustomer.s = serviceState.finished;
	}
	public void TryWithdraw(){
		building.getManager().msgWithdraw(currentCustomer.acctNum, currentCustomer.amount, this);
		currentCustomer.s = serviceState.inProgress;
	}
	public void TransferWithdrawal(){
		currentCustomer.bc.msgHereIsWithdrawal(currentCustomer.amount);
		currentCustomer.s = serviceState.finished;
	}
	public void DepositSuccessful(){
		currentCustomer.bc.msgDepositCompleted();
		currentCustomer.s = serviceState.finished;
	}
	public void CheckLoanEligible(){
		currentCustomer.s = serviceState.inProgress;
		if(currentCustomer.salary == 0){
			currentCustomer.bc.msgTransactionDenied();
			//Non- norm and will hang for now
		}
		else if(currentCustomer.salary*2 > currentCustomer.amount){
			building.getManager().msgCreateLoan(currentCustomer.amount, currentCustomer.amount/4, currentCustomer.acctNum);
			currentCustomer.bc.msgLoanGranted(currentCustomer.amount);
			currentCustomer.s = serviceState.finished;
		}
	}
	
	public void GetNewCustomer(){
		currentCustomer = null;
		building.getManager().msgAvailable(this);
	}
	// Getters
	
	// Setters
	
	// Utilities
	@Override
	public void print(String msg) {
        super.print(msg);
        AlertLog.getInstance().logMessage(AlertTag.BANK, "BankTellerRole " + this.getPerson().getName(), msg);
    }
	
	// Classes
	public class MyCustomer{
		public int acctNum;
		BankCustomer bc;
		int amount;
		int salary;
		serviceState s;
		serviceType t;
		public MyCustomer(BankCustomer bc2){
			bc = bc2;
		}
	}
}
