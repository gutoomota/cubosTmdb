# Movie Finder

## Written by Mário Augusto Mota Martins 

## Objectives

● Blockers:
> Select a specific movie to see details (name, backdrop image and overview). 

> Scroll through the list of top rated movies - including movie name and poster image. 

> Implesments the MVP design pattern, where you can find a bit on logic in the View, but most of it is in the Controller class, which acts as an Façade and is the instance of the Application

● Optionals:
> Data Persistence, adding favorites that are being stored in a local database.

> Api instance is on Presenter.

> Search for movies by entering a partial or full movie name. 

> Orientation doesn't reload the page.

## Build Details

> Target Android Version: 8.1 Oreo

> Minimum recommended Version: 8.0

## Used platform

> Android Studio 3.1.3

## Main used - frameworks

> Retrofit API v2.3.0 - API to handle REST requests

> Moshi Converter v2.3.0 - Json framework for Android and Java, used to deserialize objects returned by API in Json.

> Realm Plugin v5.4.1 - Plugin to work with SQL databases in a very abstract way, without the need of writting SQL queries.

## Get Started
> Run the code through the Andrid Studio 3.1.3 in an emulator or debug using a device.

## Details

> A Favorite system was made, in which the user can mark the movies he want to save localy. After saved as favorite, the movie will be available on the "star" button at the toolbar even when the phone is without network connection, as it's stored in a local database.

> The Strings from TmdbApi.class were stored on a xml file and the API_KEY is Distributed on string_array to make it harder to reassemble in case of a reverse engineering on the .apk file

> The Movie Search is being done by an url that was found at the TmdbAPI documentation page.

> Problem with loss of Internet connection is reported to user.

> Next page is requested to API once the user gets to the end of the list.

> Query requests made using search box runs for every character added or removed from the box, if the user delete all the characters the hint reappears and the top rated movies are requested again.

> To avoid problems with results from last requests being shown on the list, it's generated a requestKey for every request, the requestKey is a random string, with a length of 10 units. When the response of the request gets in the system the requestKey is checked to see if it is the last request made, if it is, the data is added to the list, if it is not, the data is discarted.

## API routes
>> documentation: 
>> https://developers.themoviedb.org/3

> Top rated Movies
>> url:
>> https://api.themoviedb.org/3/movie/top_rated?api_key={api_key}&language={language}&page={page}&region={region}

> Images
>> poster url:
>> https://image.tmdb.org/t/p/w154/{poster_path}?api_key={api_key}

>> backdrop url:
>> https://image.tmdb.org/t/p/w154/{backdrop_path}?api_key={api_key}

> Movie search by query
>> url:
>> https://api.themoviedb.org/3/search/movies?api_key={api_key}&language={language}&query={query}&page={page}&region={region}
