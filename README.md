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

* `base` contains the `Agent`, `Animation`, `Role`, and `Mock` abstract classes. 
* `city.agents` contains all classes which extend `Agent`.
* `city.animations` contains all classes which extend `Animation`.
* `city.animations.interfaces` contains interfaces to all classes in `city.animations`.
* `city.gui` contains various classes for displaying the program GUI. These classes are separate from the classes in `city.animations` since they don't represent any type of agent, only the chrome/controls for the GUI itself.
* `city.interfaces` contains interfaces to all classes in `city.agents` and `city.roles`.
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

