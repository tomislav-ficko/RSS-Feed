# RSS-Feed

Android app for showcasing my coding practices, developed in line with well known clean code principles.

### Requirements

Users should be able to:
- Add new RSS feeds by specifying an URL
- Remove existing feeds
- See added feeds
    - The RSS feed presentation should include the feed name, image and description
- Select a feed to open a screen containing the RSS feed items
    - The RSS feed item presentation should include the item name, image and description
- Select a feed item to access the related website/feed
    - The app can open an RSS item link in a WebView

Optionally, users can:
- Turn on notifications for new feed items, for subscribed RSS feeds (not implemented)
- Add RSS feeds to Favorites

Some requirements, such as design and optimization, are omitted on purpose. They can be implemented at will.

### Testing

The app can be used by adding any available RSS link, since the feed XML response has a specific structure that should be the same for every feed.

That being said, I noticed some feeds do not conform to the standard RSS response guidelines, and for that kind I did not manage to extract the response data (without adding more complext deserialization logic). For such feeds (with non-standard responses) the app will throw an error message saying that something went wrong while adding the feed.

The app was primarily tested using [CNN feeds](https://edition.cnn.com/services/rss/), with which the app works as expected.

## Project structure

The following picture defines project structure and how information flows between layers of the application:
<br>

![Project Structure](project-structure.png)

## Tech stack

- MVVM architecture
- Hilt
- LiveData
- Coroutines
- Retrofit
- Room
