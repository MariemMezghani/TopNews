# Overview
Top News is a news application that helps the users stay updated with trending news from 
different famous sources like CNN and BBC News. It fetches data from the internet with the NewsApi API. 
The app allows its users to explore news articles by sources . When the user clicks on an article, he can read it, share it and write a
comment.

# Intended Users
The app is for anyone who wants to stay up to date with the latest news around the world,
and who wants to have access to different popular news sources in a single app.

# Features
- allows the user to login with email or gmail in the LogIn Activity
- allows the user to choose a news source from the navigation drawer
- displays a list of the latest news from the chosen source in the MainActivity
- When the user clicks on a news list item, he will be redirected to the Detail Activity to read the article
- In the Detail Activity, the user can share the article
- In The Detail Activity, the user can write a comment and read other users comments
- App has a home screen widget to display the chosen News Title

# Screenshots
![image](https://user-images.githubusercontent.com/35550711/73890267-53e01780-4850-11ea-80b5-5b274b547383.png) ![image](https://user-images.githubusercontent.com/35550711/73890342-84c04c80-4850-11ea-86ba-55ace7fdf666.png)

![image](https://user-images.githubusercontent.com/35550711/73890463-cbae4200-4850-11ea-87d8-c8aa39ab122d.png) ![image](https://user-images.githubusercontent.com/35550711/73890529-f00a1e80-4850-11ea-91e2-da5c4301754e.png)

![image](https://user-images.githubusercontent.com/35550711/73890693-5000c500-4851-11ea-98ac-39d12ef3461e.png) ![image](https://user-images.githubusercontent.com/35550711/73890747-73c40b00-4851-11ea-9c95-b194f04bf86f.png)

# Libraries
- Picasso ( version:2.5.2 ) to display images from the web
- Retrofit ( version:2.4.0 ) to fetch JSON data from the NewsApi.org

# Google Play Services
- Firebase authentication: allow users to sign in with email or google
- Firebase messaging: allow users to write comments

# License
Copyright 2019 Mariem Mezghani

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
