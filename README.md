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
 - The project contains a city view alongside a building view alongside a control panel. The city view includes roads and buildings.
 - A building view displays the internal structures of the buildings. 
 - People travel between buildings by car or bus or walking and assume roles appropriate to the buildings. They can be an employee (i.e. manager, employee, waiter, etc.) of the business the building represents, or a customer. Customers just walk around, go to banks, eat at restaurants, eat at home, etc. And sleep. They like to sleep.
 - The city operates on 2, 12-hour shifts. Half of the city's occupants are workers while the other half are people or customers at a given time. When people are not working, they can decide to stay at home, go to a restaurant, go the bank, or the market depending on their disposition. Workers actually work 24 hour shifts.
 
## Project Contributions
### John Timms (22%)

John is responsible for the entire architecture of the project's back end. The general interaction of Agents, Buildings, Roles, etc. was all designed by John, and he assisted the rest of the team in developing roles that could interact with the rest. John wrote the PersonAgent class and made skeletons for all of the project's code files. John's restaurant is RestaurantTimms; it has the market partially implemented and no bank or revolving stand implementation. John assisted the team by explaining various git strategies and managed merges. The PersonAgent does not have unit tests, but there are no known issues with it. Problems where people do not ever leave a building or job are caused by improper integration with the framework.
 
### David Zhang (21%)

David is responsible for the GUI architecture. Animations, the city view, building view, control panel, all images, and trace panel were all implemented by David. David also implemented the transportation system. Both cars and people have collision detection with each other, but sometimes it doesn't work at intersections. Cars and buses and people walking all have sophisticated navigation systems - a faux A Star for vehicles, and an actual A Star designed by David for walkers. Buses stop at bus stops, and people animate to them as well. David wrote the revolving stand which everyone uses in their shared-data restaurants. In addition to writing all GUI code, David also provided functions in Application class (where the main function resides) to make integration of all people, buildings, transportation, etc. to work properly.

### John Francis (19%)

Designed and implemented bank, which is fully tested and functional within the city. Also provided methodology to allow integration with Restaurants and Markets. Wrote cash handling methods in the building and person class to allow directDeposits by businesses. Restaurant is integrated into the city, but it isn't working. Gets hung up on cashier because I simply did not have time to change the way the building was initialized. Created the standard parseConfig and runtime scenario, as well as the beautiful, non-functional scenario tab. Disclosure: Restaurant testing is virtually non-existent, except for the Cashier Restaurant does not finish normative. Bank robbery scenario was not implemented although a little BankRobber guy does run around and live with the other employees to gain some pretty stellar intel for his big heist. Did not integrate market into restaurant. **Was responsible for implementing all scenarios and largely failed. Lacked infrastructure for closing buildings and creating collisions, and as stated before, did not implement BankRobbery upon command.

### Shirley Chung (19%)

Market

 - Shirley designed and implemented market interactions. This required creating all the roles associated with market, getting them to work with personAgent, integrating with bank to deposit money, and integrating with restaurants. JUnit testing worked for all the roles except for DeliveryPerson. A significant design change was implemented late in the project that did not allow for a complete change of the test. Animation for the market is implemented. It works for both in-person and delivery customers. The first customer in line sometimes does not get helped and employees freeze when they talk to the cashier. The delivery car leaves, goes to a restaurant, and returns. The code for handling a closed restaurant upon delivery was implemented, but his scenario was never tested.

Restaurant

 - The RestaurantChung is integrated into the city. It can handle multiple customers and the producer-consumer scenario works. A scenario running both producer-consumer and traditional messaging has not been tested. The cashier successfully interacts with the bank. The customer and waiters will sometimes freeze after ordering.

### Ryan Choi (19%)
-All housing logic, e.g. clear room after removing person; how often/much maintenance is added to rent intervals.

-All housing graphics, and animation. e.g. Difference between apartment and house panels.

-Landlord can own >1 residence. Landlord doesn't have to live in residence. Apartments can share residents.

-Everything in the "Edit Building" Tab, for live changes (e.g. force withdrawal by setting cash to 0).

-Restaurant integration: producer-consumer, bank transactions, market orders WERE functional but not sure.

-Integrated bank and market into restaurant, with ordering OK, withdraw OK, deposit OK.
