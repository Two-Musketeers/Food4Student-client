# Food4Student

## How to build

* Step 1: Create a Firebase project. 

Add an Android app to that, download `google-services.json` file and put it inside `/app` folder.

Yes you will need to verify your app SHA, Google will be your best friend.

* Step 2: Build variables

Make a Here SDK account, and get the keys.

Copy the content of file `secret.example.properties` into a file named  `secret.properties` in the root directory.

Add your needed values into each line. For example:

```
HERE_API_KEY=YOUR_API_KEY
HERE_API_SECRET_KEY=YOUR_SECRET_KEY
BACKEND_URL=https://example.com/api/
```

**WARNING:** Make sure there is a leading `/` after the backend url. Android shennanigans...

* Step 3: Build the project along with the backend
* Step 4: Run the project
* Step 5: Enjoy the app
