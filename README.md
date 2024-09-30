# GeoRocks App

Welcome to **GeoRocks**, a comprehensive mobile application designed for geology enthusiasts and professionals. With GeoRocks, you can easily catalog, view, and manage a variety of rock types. The app includes detailed information on rock names, types, and origins, making it the perfect tool for anyone who loves studying rocks!

![GeoRocks Preview](video_app.gif)

## Features

- **Rock Catalog**: Add, update, and delete records of rocks, with detailed information like name, type, and origin.
- **Image-Based Display**: See visually stunning icons of rock types based on their origin: plutonic, volcanic, or metamorphic.
- **Interactive UI**: A sleek and modern UI with easy-to-use interfaces and beautiful card designs to showcase each rock in the catalog.
- **Floating Action Button**: Quickly add new rocks with just a tap on the floating action button.
- **Responsive Feedback**: Snackbar messages to confirm actions like saving, updating, or deleting a rock.

## How It Works

The GeoRocks app is built using **Kotlin** and follows Android's **MVVM architecture**. The data is managed using **Room Database**, with **RecyclerView** used to display the list of rocks. 

Each rock is categorized based on:
- **Name**: The name of the rock (e.g., Granite, Basalt, Marble).
- **Type**: The classification of the rock (e.g., Igneous, Sedimentary, Metamorphic).
- **Origin**: The origin of the rock (e.g., Plutonic, Volcanic, Metamorphic).

## Rock Catalog Preview

You can add rocks with names like Granite, Basalt, and Marble. Depending on the rock's origin, a specific icon will be displayed. For example:
- **Plutonic** rocks will show an icon of a rugged, textured rock.
- **Volcanic** rocks will display an icon with sharp, jagged features.
- **Metamorphic** rocks will have a smooth, layered icon.

## Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/your-username/georocks.git
   ```

2. **Open in Android Studio:**
   Open the project in Android Studio and let it sync.

3. **Run the app:**
   Once synced, you can run the app on an emulator or physical device.

## How to Use

- **Add Rocks**: Tap on the **Floating Action Button (FAB)** at the bottom-right corner to add a new rock.
- **Edit Rocks**: Tap on any rock in the list to edit its details.
- **Delete Rocks**: While editing, you can delete the rock by tapping the "Delete" button.
  
All changes are immediately saved to the Room Database and reflected in the UI.

## Technology Stack

- **Kotlin**: The programming language used for building the app.
- **Room Database**: To store rock records locally.
- **RecyclerView**: For efficiently displaying the rock list.
- **View Binding**: To easily interact with views.
- **Material Design Components**: For a modern and smooth user experience.
- **Coroutines**: To handle database operations asynchronously.

## Screenshots

![App Screenshot 1](screenshots/screen1.png)
![App Screenshot 2](screenshots/screen2.png)

## Future Enhancements

- Add filtering and searching options.
- Include support for different languages.
- Add more detailed rock classifications and data.

## Credits

This app was developed by Carlos Ignacio Padilla Herrera. Special thanks to the open-source libraries,resources and AHPM that made this app possible.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for more details.
```
