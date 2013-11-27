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
The project contains a city view alongside a building view. The city view includes roads and buildings, and the building view displays the internal structures of the buildings. People travel between buildings by car or bus and assume roles appropriate to each building. Individuals can be an employee (i.e. manager, employee, waiter, etc.) of the business the building represents, or a customer. The city operates on 2, 12-hour shifts. Half of the city's occupants are workers while the other half are people or customers, at a given time. When people are not working, they can decide to stay at home, go to a restaurant, go the bank, or the market depending on their disposition. People can take on multiple roles. Most of the time, they only act in one role. In some cases, their job requires them to take on multiple. For example, a restaurant cook may also be a market customer.

There is still work to be done in the animations. People do not walk in the streets. Banks and markets are not animated. The user cannot interact with the gui to influence behaviors in the city. Shift and occupation changes have been designed, but not tested.

## Project Contributions
### John Timm (22%)
* Person 
* Project Structure
  - Designed project structure and provided team with a package to import
* Git Management
  - Assisted team members with git issues and merge conflicts
 
### David Zhang (21%)
* City Animation
  - Designed base city gui interface and procedure for adding other components
* Transportation
  - Implemented city transportation, including cars and buses

### John Francis (19%)
* Bank
  - Designed and implemented bank

### Shirley Chung (19%)
* Market
  - Designed and implemented market
* Market and Restaurant Integration
  - Integrated market interactions with personal market. Attempted to assist with market integrations of other restaurants.

### Ryan Choi (19%)
* Housing
  - Designed and implemented housing components
