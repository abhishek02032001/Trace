Trace - A Covid Trcaer App

PROBLEM STATEMENT:

In this time of pandemic, the biggest problem our country is facing is preventing the spread of covid-19(Coronavirus).It is very important to control the spread of this virus. Simply, at present we have very few options like lockdown, 14 day quarantine, etc.Lockdowns are economically harmful. 14 day home quarantine period is not trustworthy, people can easily violate this rule without letting any concerned authorities know.So, in this era of technology relying on trust of people for such a major concern is not ok. In the current situation, if a person is tested positive then the government traces the person's building, close relatives and the surrounding area but it is not effective as the person could have travelled to other places aswell.Although, there are many applications on market dealing with other major problems for covid-19 but then too there are no such applications focusing on this problem fully. A perfect application can easily solve this problem too.

PROPOSED SOLUTON:

This project is usually a tracking system. It can be used to track the people's activity who are under quarantine period. It can also be used for travellers from other countries to keep their track like where they visited during the tour.If we have this data we can easily know who violated the quarantine period and also can track the places where the tourists have visited.This application uses gps tracking and once it's started an automatic line is drawn in the places on the map screen, which shows the routes where the person travelled along with phone. There is a statistics page which shows the statistics of the travel like distance, speed, time, etc. With this solution authorities can easily track concerned people and prevent spreading of covid-19 to some extent.This solution requires people to be coperative so that government can trace their travel with which tracing can be made easier and effective.
![trace1(0)]<img src="https://user-images.githubusercontent.com/74093122/148576058-3f7f6ed1-5c6a-493c-bde8-f9daf8dad56d.jpeg" width="100" height="100">
![trace1 (1)](https://user-images.githubusercontent.com/74093122/148576132-e60ca68f-5a37-4ebd-a8b0-6bb65c286a21.jpeg)
![trace1 (2)](https://user-images.githubusercontent.com/74093122/148576150-4480d3e9-41c6-4fc0-8257-4357c8234a37.jpeg)
![trace1 (3)](https://user-images.githubusercontent.com/74093122/148576163-1ea351b8-7b6a-4816-9e11-b40928c179a0.jpeg)
![trace1 (4)](https://user-images.githubusercontent.com/74093122/148576179-8bbe2a90-bb96-43e9-a0ef-41d4338f6324.jpeg)
![trace1 (5)](https://user-images.githubusercontent.com/74093122/148576190-f4940e94-2a3e-493c-aab1-2d030c12f920.jpeg)
![trace1 (6)](https://user-images.githubusercontent.com/74093122/148576202-8504d03c-f3f3-4704-8367-2c8fed99094b.jpeg)
![trace1 (7)](https://user-images.githubusercontent.com/74093122/148576213-5a907891-00cb-4c71-901d-6398af9c9fed.jpeg)
![trace1 (8)](https://user-images.githubusercontent.com/74093122/148576233-2049921f-9132-4c13-b131-24024c4474df.jpeg)


FUNCTIONALITIES & CONCEPTS Used :

Mvvm architecture: MVVM architecture is used in development of this application. MVVM architecture is a Model-View-ViewModel architecture that removes the tight coupling between each component.
Constraint layout: Most of the activities in the app uses a flexible constraint layout, which is easy to handle for different screen sizes.
Navigation graph: We have used Navigation graph to navigate between the fragments. A navigation graph is a resource file that contains all of your app's destinations along with the logical connections, or actions, that users can take to navigate from one destination to another.
Recycler view: We have used Recycler view to show the list of activities in the stats page. 
Room database: We have used room db to store the data.
Basics of dragger hilt: we have also used dragger hilt for dependency injection

Application link & future scope:
You can access the app : https://github.com/abhishek02032001/Trace/releases/tag/v1.0.0

Once the app is fully functional we can add more features like directly observing the activity of users from authorities' end and if the user is violating the rules they should automatically get warnings and alerts. Stopping the tracking of app should be inaccessible by users this feature is also to be implemented.
