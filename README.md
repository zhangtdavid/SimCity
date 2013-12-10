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

* `city` contains the `Agent`, `Animation`, `Building`, `Role`, `MockAgent`, `MockAnimation`, and `MockRole` abstract classes. It also contains the `Application` class.
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

Please read the [Coding Standards](https://github.com/usc-csci201-fall2013/team17/wiki/Coding-Standards) wiki for full details.

## Project Outline
 - The project contains a city view alongside a building view. The city view includes roads and buildings.
 - A building view displays the internal structures of the buildings. 
 - People travel between buildings by car or bus and assume roles appropriate to the buildings. They can be an employee (i.e. manager, employee, waiter, etc.) of the business the building represents, or a customer. 
 - The city operates on 2, 12-hour shifts. Half of the city's occupants are workers while the other half are people or customers at a given time. When people are not working, they can decide to stay at home, go to a restaurant, go the bank, or the market depending on their disposition.

## Problems
 - Banks and markets are not yet fully integrated.
 - Tests for GUI have not been written. Roles and agents are only partially tested.
 - There is still work to be done in the animations. People do not walk in the streets. Banks and markets are not animated. The user cannot interact with the GUI to influence behaviors in the city.
 - Some restaurants do not work.
 - The console has many errors, mostly due to partial integration of the market into restaurants.
 - Everyone goes to work or goes to eat and then just stays there.
 
## Project Contributions
### John Timms (22%)

John is responsible for the entire architecture of the project, except for animations. The general interaction of Agents, Buildings, Roles, etc. was all designed by John, and he assisted
the rest of the team in developing roles that could interact with the rest. John wrote the PersonAgent class and made skeletons for all of the project's code files. John's restaurant is 
RestaurantTimms; it has the market partially implemented and no bank or revolving stand implementation. John assisted the team by explaining various git strategies and managed merges.
The PersonAgent does not have unit tests, but there are no known issues with it. Problems where people do not ever leave a building or job are caused by improper integration with the framework.
 
### David Zhang (21%)

David is responsible for the GUI architecture. Animations, the city view, building view, control panel, and trace panel were all implemented by David. David also implemented the transportation
system. Some cars roam infinitely. Buses do not stop in the animation, but they are working correctly. David wrote the revolving stand which everyone uses in their shared-data restaurants. In
addition to writing all GUI code, David also integrated everyone's code in the `Application` file so that everything could run in the same simulation.

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
  - Created all roles associated with market, got working with personAgent and integrated with bank
  - Did JUnit testing for all roles
  - Completed animation for in person and delivery interactions
  - Did not test with multiple employees
  - Had some instances where customer did not successfully enter markt. Not sure if this was due to the market, car, or animation.
  - Market Delivery animation worked in restaurantChungAnimation branch, but had problems integrating car animation in master
  - Implemented code for handling a closed restaurant upon delivery, but could not set up scenario in application and did not test
* Restaurant
  - Restaurant integrated into the city
  - Can handle multiple customers
  - Producer-consumer scenario works
  - Have not tested with both producer-consumer and traditional messaging
  - Bank interaction works
  - Customer will sometimes freeze after ordering
  - While testing in restaurantChungAnimation branch, froze after cook ordered from market. After changes to city animations in master, this may have been fixed, but did not test
* Market and Restaurant Integration
  - Integrated with personal restaurant, provided instructions for integration with other restaurants, but it would have been helpful if they were posted earlier

### Ryan Choi (19%)
-All housing logic, e.g. clear room after removing person; how often/much maintenance is added to rent intervals
-All housing graphics, e.g. difference between apartment and house panels
-Everything in the "Edit Building" Tab, for live changes (e.g. force withdrawal by setting cash to 0)
-Restaurant integration complete: producer-consumer, market orders, bank transactions.
-Integrated bank and market into restaurant, with ordering OK, withdraw OK, deposit OK. 
