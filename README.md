SimCity 201
===========

## Getting Started

1. Clone this git repository to your local machine.
2. Open Eclipse, go to File > Import... and select "Projects from Git". (**If you do not see this option:** Go to Help > Eclipse Marketplace... and install the package named EGit.) 
3. Choose "Existing local repository" and find this repo which you've just cloned. Use the "Import existing projects" wizard and click the checkbox next to the project named **SimCity201**.
4. Click continue/finish, you should now have the entire project loaded into Eclipse.

It is also suggested that you install EclEmma from the Eclipse Marketplace. This will be invaluable when writing tests. Contact @[timms](https://github.com/timms) if you have questions about using code coverage tools.

## Project Structure 

The SimCity201 project is divided between two code folders. `lib` is insignificant, it simply includes some 3rd-party libraries used by the Agent code. All of the important code is contained in `src`.

Inside `src` are multiple packages:

* `city` contains the `Agent`, `Animation`, `Building`, `Mock`, and `Role` abstract classes. It also contains the `Application` class.
* `city.agents` contains all classes which extend `Agent`.
* `city.animations` contains all classes which extend `Animation`.
* `city.animations.interfaces` contains interfaces to all classes in `city.animations`.
* `city.buildings` contains all classes which extend `Building`.
* `city.gui` contains various classes for displaying the program GUI. These classes are separate from the classes in `city.animations` since they don't represent any type of agent, only the chrome/controls for the GUI itself.
* `city.interfaces` contains interfaces to all classes in `city.agents` and `city.roles`. Since buildings do not require testing, they do not have interfaces.
* `city.roles` contains all classes which extend `Role`.
* `city.tests` contains test classes for all classes in `city.agents` and `city.roles`.
* `city.tests.gui.mock` contains mocks for all classes in `city.animations`.
* `city.tests.mock` contains mocks for all classes in `city.agents` and `city.roles`.
* `utilities` contains various classes used throughout the project.

## Programming Conventions

For ease of organization, collaboration, and readability, the following conventions have been established for `Agent` and `Role` (and their interfaces) code files:

* Please order your methods in the following way: Data, Constructor, Messages, Scheduler, Actions, Getters, Setters, Utilities, Classes (for an example of this format, see `city.agents.PersonAgent`, `city.roles.WaiterRole`, etc.) Please keep all headers in order and in place, even if there is nothing under some of them.

## Other Considerations

* No conventions have yet been established for the organization of methods in `Animation` or their interfaces.
* The overall design of the project is still in flux. So far, there has been very little discussion of how animations will work. In particular, while we are used to animating multiple agents, there is the new consideration of animating the inside of buildings which agents can enter and exit. Once this issue is decided, the structure of the project will most likely be partially modified.


## Project Status
 - The project contains a city view alongside a building view. The city view includes roads and buildings, and the building view displays the internal structures of the buildings. People travel between buildings by car or bus and assume roles appropriate to the buildings. They can be an employee (i.e. manager, employee, waiter, etc.) of the business the building represents or a customer. The city operates on 2, 12-hour shifts. Half of the city's occupants are workers while the other half are people or customers at a given time. When people are not working, they can decide to stay at home, go to a restaurant, go the bank, or the market depending on their disposition.
 - Banks and markets are not yet fully integrated. Markets work in one of our branches, but we didn't get a chance to merge it with master.
 - Tests for gui have not been written. All roles and agents partially tested.
 - There is still work to be done in the animations. People do not walk in the streets. Banks and markets are not animated. The user cannot interact with the gui to influence behaviors in the city.

## What Happens When You Run The Code
 - People will get up at various times, and they will go in their cars to their respective places.
 - Most people will go to the restaurant to work. Some take the cars which are pink, some take the lone bus.
 - The people who aren't working are landlords. They prefer to go into restaurants to eat, and do just that.
 - Some restaurants aren't working, we're working on it! They do work by themselves though for some reason, in their branches.
 - There's a LOT of errors in the console that we're aware of - they're mostly NPE's from partially integrated markets.
 - Everyone goes to restaurant, eats/works, and just stays there. It's kinda weird, but roles don't really do anything but go to restaurants.

## Project Contributions
### John Timms (22%)
* Person 
  - Laid out entire personagent and integrated with all roles
* Project Structure
  - Designed project structure and provided team with a package to import
  - Laid out base class and interface code, such as Agent, Roles, Animation, etc.
  - Got time working for a day in the city. Not working, but lets people get up, do stuff, and go to restaurant to work/eat.
* Git Management
  - Assisted team members with git issues and merge conflicts
  - Managed all branches and got integration working smoothly on the last day
* Restaurant
  - Restaurant is integrated into the city and working. Only animates when you click on it. Not sure why about this last part - David
 
### David Zhang (21%)
* City Animation
  - Designed base city gui interface and procedure for adding other components
  - Put together animations, city view, building view, control panel, and trace panel
  - Trace panels for some filters work very limited for some reason. Not sure why.
* Transportation
  - Implemented city transportation, including cars and buses. Some cars are roaming around infinitely. Not sure why. Cars never collide :D
  - Bus doesn't appear to stop at any stops, but it does - just filter the trace panel to "Bus"
* New Waiter
  - Created new shared data waiter. You can see this in the RestaurantZhang
* Restaurant
  - Integrated restaurant with new shared data waiter. It's in the upper left.
* Integration
  - Integrated everyone's roles and buildings and restaurants into the application so everything would run in the same city.

### John Francis (19%)
* Bank
  - Designed and implemented bank. Bank is not yet fully functioning in the city, but works by itself in a branch. You can see a small demo that interacts with RestaurantChoi
  - Provided methodology to allow integration with Restaurants and Markets
* Buildings
  - Wrote cash handling methods in the building and person class to allow directDeposits by businesses
* Restaurant
  - Restaurant integrated into the city, but it isn't working. Not sure why - David integrated it into the restaurant. 

* Disclosure
  - Testing exists for the Teller and Manager, but not the Customer. The Manager testing only covers the directDeposit funcitonality
  - Restaurant testing is virtually non-existent, except for the Cashier
  - Loan payment functionality exists in local branch but was not done in time for integration (EDIT: may have gotten in)
  - Internal BankGui was not completed
  - Ironically, personal restaurant was not fully integrated with Bank (EDIT: may have gotten in)
  - All Bank interactions have been tested in runtime and do work perfectly, although the mechanism to pay employees after they have been setInactive only exists in local BankBranch and was not done in time for integration (EDIT: may have gotten in)
  - Restaurant stopped working during final integation due to errors in RestaurantPanel. Was implemented and working correctly in commit 1bf454f98b70f3377da416038285c1d7d2bfabe2

### Shirley Chung (19%)
* Market
  - Designed and implemented market
  - Got it working in a branch, but didn't have time to fully integrate with master
  - Created all roles associated with market, and integrated with bank.
* Market and Restaurant Integration
  - Integrated market interactions with personal market. Attempted to assist with market integrations with other restaurants.
* Restaurant
  - Restaurant integrated into the city, but it isn't working. Not sure why - David integrated it into the restaurant. 


### Ryan Choi (19%)
* Housing
  - Designed, implemented residential components. One-to-many landlord-to-home relations, rent, maintenance, etc.
  - The room designs are in the code but not implemented. John's activity on PersonAgent made ResidentRole very straightforward.
  - See branch rchoi for JUnit tests.
  - This restaurant utilizes the bank for deposits.
  - This restaurant did not completely implement markets, though I believe it could have. The market receives the request for more food, but did not deliver. (complexities with v2.2 design and new delivery-truck mechanisms)
  - "Full disclosure" I'm inclined to believe this percentage should be a bit lower, excuses of exams or research aside. 
  - That being said this shouldn't be a problem going into V2 since midterms, etc. are over. The lack of House/apt GUI is my responsibility, i.e. not knocking out difference between intra-house person and resident behavior, or just doing the person animations because I very well could have.
  - We will have a good project for V2.
