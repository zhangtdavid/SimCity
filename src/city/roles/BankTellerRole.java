package city.roles;

import java.util.concurrent.Semaphore;

import trace.AlertLog;
import trace.AlertTag;
import city.animations.BankTellerAnimation;
import city.bases.JobRole;
import city.buildings.interfaces.Bank;
import city.roles.interfaces.BankCustomer;
import city.roles.interfaces.BankTeller;

public class BankTellerRole extends JobRole implements BankTeller {
	
	// Data
	private Bank building;
	private int boothNumber;
	private boolean wantsInactive = false;
	public MyCustomer currentCustomer = null;
	private BankTellerAnimation gui = null;
	private Semaphore atDestination = new Semaphore(0,true);
	
	// Constructor
	public BankTellerRole (Bank b, int shiftStart, int shiftEnd){
		building = b;
		this.setWorkplace(b);
		this.setSalary(Bank.WORKER_SALARY);
		this.setShift(shiftStart, shiftEnd);
	}

	// Messages
	@Override
	public void msgAddressCustomer(BankCustomer bc){
		print("Address Customer msg received");
		currentCustomer = new MyCustomer(bc);
		currentCustomer.s = SERVICE_STATE.needsService;
		stateChanged();
	}
	
	@Override
	public void msgHereIsAccount(int acctNum){
		print("Here is Account msg received");
		if(currentCustomer.t == SERVICE_TYPE.deposit){
			currentCustomer.acctNum = acctNum;
			currentCustomer.s = SERVICE_STATE.newAccount;
			stateChanged();
		}
	}
	
	@Override
	public void msgWithdrawalFailed(){
		print("Withdrawal failed msg received");
		if(currentCustomer.t == SERVICE_TYPE.withdrawal){
			currentCustomer.s = SERVICE_STATE.failed;
			stateChanged();
		}
	}
	
	@Override
	public void msgTransactionSuccessful(){
		print("Transaction successful msg received");
		if(currentCustomer.t == SERVICE_TYPE.deposit)
			currentCustomer.s = SERVICE_STATE.confirmed;
		else if(currentCustomer.t == SERVICE_TYPE.withdrawal)
			currentCustomer.s = SERVICE_STATE.confirmed;
		stateChanged();
	}
	
	@Override
	public void msgWithdraw(int acctNum, int money, int salary){
		print("Withdraw message received from Customer");
		currentCustomer.s = SERVICE_STATE.pending;
		currentCustomer.t = SERVICE_TYPE.withdrawal;
		currentCustomer.acctNum = acctNum;
		currentCustomer.amount = money;
		currentCustomer.salary = salary;
		stateChanged();
	}
	
	@Override
	public void msgDeposit(int money, int acctNum){
		print("Deposit message received");
		currentCustomer.s = SERVICE_STATE.pending;
		currentCustomer.t = SERVICE_TYPE.deposit;
		currentCustomer.amount = money;
		currentCustomer.acctNum = acctNum;
		stateChanged();
	}
	
	@Override
	public void msgDoneAndLeaving() {
		print("Done and Leaving message received");
		currentCustomer.s = SERVICE_STATE.done;
		stateChanged();
	}
	
	// Scheduler
	@Override
	public boolean runScheduler() {
		// TODO Auto-generated method stub
		if(wantsInactive && currentCustomer == null){
			super.setInactive();
			wantsInactive = false;
			this.getPerson().setCash(this.getPerson().getCash() + Bank.WORKER_SALARY);
			gui.DoLeave();
			return false;
		}
		if(currentCustomer != null){
			if(currentCustomer.s == SERVICE_STATE.needsService){
				ServiceCustomer();
				return true;
			}
			if(currentCustomer.t == SERVICE_TYPE.deposit){
				if(currentCustomer.s == SERVICE_STATE.pending){
					TryDeposit();
						
					return true;
				}
				if(currentCustomer.s == SERVICE_STATE.newAccount){
					GiveAccountNumber();
					return true;
				}
				if(currentCustomer.s == SERVICE_STATE.confirmed){
					DepositSuccessful();
					return true;
				}
			}
			if(currentCustomer.t == SERVICE_TYPE.withdrawal){
				if(currentCustomer.s == SERVICE_STATE.pending){
					TryWithdraw();
					return true;
				}
				if(currentCustomer.s == SERVICE_STATE.confirmed){
					TransferWithdrawal();
					return true;
				}
				if(currentCustomer.s == SERVICE_STATE.failed){
					CheckLoanEligible();
					return true;
				}
			}

			if(currentCustomer.s == SERVICE_STATE.done){
				GetNewCustomer();
				return true;
			}
		}
		return false;
	}
	
	// Actions
	private void ServiceCustomer(){
		if(gui!=null)
			gui.DoString("May I help the next customer?");
		currentCustomer.bc.msgWhatDoYouWant(boothNumber, this);
		currentCustomer.s = SERVICE_STATE.inProgress;
	}
	
	private void TryDeposit(){
		if(gui!=null){
		gui.DoGoToVault();
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gui.DoGoToStation();
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		currentCustomer.s = SERVICE_STATE.inProgress;
		building.getManager().msgTryDeposit(currentCustomer.amount, currentCustomer.acctNum, this);
	}
	
	private void GiveAccountNumber(){
		if(gui!=null)
			gui.DoString("Your account number is " + currentCustomer.acctNum);
		currentCustomer.bc.msgAccountCreated(currentCustomer.acctNum);
		currentCustomer.s = SERVICE_STATE.finished;
	}
	
	private void TryWithdraw(){
		if(gui!=null){
		gui.DoGoToVault();
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gui.DoGoToStation();
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		building.getManager().msgWithdraw(currentCustomer.acctNum, currentCustomer.amount, this);
		currentCustomer.s = SERVICE_STATE.inProgress;
	}
	
	private void TransferWithdrawal(){
		if(gui!=null)
			gui.DoString("Here is your withdrawal");
		currentCustomer.bc.msgHereIsWithdrawal(currentCustomer.amount);
		currentCustomer.s = SERVICE_STATE.finished;
	}
	
	private void DepositSuccessful(){
		if(gui!=null)
			gui.DoString("Your deposit was successful");
		currentCustomer.bc.msgDepositCompleted();
		currentCustomer.s = SERVICE_STATE.finished;
	}
	
	private void CheckLoanEligible(){
		currentCustomer.s = SERVICE_STATE.inProgress;
		if(currentCustomer.salary == 0){
			if(gui!=null)
				gui.DoString("Your transaction was denied");
			currentCustomer.bc.msgTransactionDenied();
			
			//Non- norm and will hang for now
		}
		else if(currentCustomer.salary*2 > currentCustomer.amount){
			building.getManager().msgCreateLoan(currentCustomer.amount, currentCustomer.amount/4, currentCustomer.acctNum);
			if(gui!=null)
				gui.DoString("You have been granted a loan");
			currentCustomer.bc.msgLoanGranted(currentCustomer.amount);
			currentCustomer.s = SERVICE_STATE.finished;
		}
	}
	
	private void GetNewCustomer(){
		currentCustomer = null;
		building.getManager().msgAvailable(this);
		
	}
	
	// Getters
	
	// Setters
	
	@Override
	public void setActive() {
		print("Teller has been set active");
		building.getManager().msgAvailable(this);
		super.setActive();
		if(gui != null){
			gui.DoGoToStation();
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}
	
	@Override
	public void setInactive(){
		if(currentCustomer == null){
			super.setInactive();
			this.getPerson().setCash(this.getPerson().getCash() + Bank.WORKER_SALARY);
			gui.DoLeave();
		}	
		else{
			building.getManager().msgUnavailable(this);
			wantsInactive = true;
		}
	}
	
	// Utilities
	
	@Override
	public void print(String msg) {
		this.getPerson().printViaRole("BankTeller", msg);
        AlertLog.getInstance().logMessage(AlertTag.BANK, "BankTellerRole " + this.getPerson().getName(), msg);
    }
	
	// Classes
	
	public class MyCustomer{
		public int acctNum;
		BankCustomer bc;
		int amount;
		int salary;
		SERVICE_STATE s;
		SERVICE_TYPE t;
		public MyCustomer(BankCustomer bc2){
			bc = bc2;
		}
	}

	public void setGui(BankTellerAnimation anim) {
		gui = anim;
		// TODO Auto-generated method stub
		
	}

	public void msgAtDestination() {
		// TODO Auto-generated method stub
		atDestination.release();// = true;
		stateChanged();
	}
	
	public void setBoothNumber(int place){
		boothNumber = place;
	}
}
