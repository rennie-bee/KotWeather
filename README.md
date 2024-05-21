# KotWeather

## Description
Kotweather provides services to query the current weather conditions of a city.

## Functionalities
- A text input where users can enter a location.
- A button to initiate the search for the location's weather information.
- The ability to display the following information about the location:
  - City, Current temperature.
  - Weather conditions (e.g., cloudy, sunny, rainy).
  - The minimum and maximum temperature for that time.
  - Sunset and Sunrise Time
  - Wind Speed, Pressure, Humidity

## Challenges Faced
From my perspective as the developer, here are two potential technical challenges I may face in this project:

**Handling API Integration and Error Scenarios**:
   - Integrating with the OpenWeatherMap API and handling the various API responses, including:
     - Parsing the JSON data returned from the API and mapping it to the appropriate data structures in my Kotlin app.
     - Handling error scenarios when the API returns an error, such as an invalid city name or a failed network request.
     - Providing meaningful error messages to the user based on the API response.
   - Implementing the ability to search for a location and display the relevant weather information.
   - Ensuring my app can handle edge cases, such as when the user enters an invalid city name or when the device is offline.

**Implementing the UI and User Interaction**:
   - Designing and implementing the user interface (UI) using Figma, which includes the following components:
     - A text input field for the user to enter a location.
     - A button to initiate the search for the location's weather information.
     - Displaying the weather information in a visually appealing and organized manner.
   - Coordinating the user interactions (e.g., text input, button click) with the API integration and updating the UI accordingly.
   - Handling the user's current location and displaying the weather information for that location by default, using the device's geolocation capabilities.
   - Ensuring the UI is responsive and provides a smooth user experience across different device sizes and orientations.

These technical challenges will require me, as the developer, to have a good understanding of Kotlin, Android development, API integration, and UI/UX design principles. Addressing these challenges will be crucial to delivering a functional and user-friendly mobile application that meets the requirements of the assignment.

## Video Showcase
https://drive.google.com/file/d/1lseMxPCTYGXvTzJtTuCJ9rWY2USp2QE3/view?usp=sharing

## Figma Design
https://www.figma.com/design/1soLm5XzIxt93vHUaCocz2/Untitled?node-id=0%3A1&t=zxrPe3TzQAMS7wk5-1
