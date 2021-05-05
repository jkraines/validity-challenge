# Duplicate Checker
This project was challenging for me because it involved a tech stack that I am completely new to. The largest challenge was getting it to run on my machines. My PC can't run docker, so that never happened.
My mac was able to run it after a little while with some installation and subsequent debugging of that installation. The implementation of the app itself was easy, the starter was well laid out, and it was
very straightforward to expand on it while maintaining a good project structure. As for my duplicate checking implementation, I used the levenshtein algorithm to compare the different records, with a certain
threshold to determine whether or not two entries constituted a match. Altogether, my solution was decent, but could be improved by using levenshtein on each field in the entries rather than the entry as a whole.
This will eliminate false positives from things such as commas, and lead to a more accurate result. I didn't implement this solution as I thought of it late, and had already exceeded the amount of time I had
allotted to work on this challenge.

## Development Setup

During development, you may simply work on front-end and backend with independent tools.

For PC users: For all the `./mvnw` commands mentioned below use `mvnw` instead.

### To run the back-end:

```bash
# Move to the backend directory
cd monolithstarter-svc

# Start the application in dev mode
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

You will now have a host running at [localhost:8080](http://localhost:8080/)

### To run the front-end:

```bash
# Move to the right directory
cd monolithstarter-static

# Install dependencies
yarn

# Start the application in dev mode
yarn run start
```
You will now have your frontend running at [localhost:9000](http://localhost:9000/)

To build frontend for production with minification:

```bash
yarn run build
```

The build artifacts will be stored in the `build/` directory


## Build / Deployment

### Using the right version of node.js

The repo is setup to run a specific version of node.js.  This version is managed in the `.nvmrc` file in the root directory.

In order to avoid errors related to node engine mismatches, make sure to run `nvm use` upon entering the root directory in each new terminal session. It will automatically download and set the active version to the correct one.

Pro-tip: If you're using Zsh as your bash client, there is a plugin for automatically switching node versions when entering a new directory (automatically triggers `nvm use` and  loads the version based on the directory’s `.nvmrc` config) [nvm-auto](https://github.com/dijitalmunky/nvm-auto)

Note: WINDOWS only - `.nvmrc` does not work on Windows, so `nvm use` in the root directory will not work as above. You must manually install the node.js version that needs to be used using `nvm install [version]` and then using `nvm use [version]` For more information, see this issue, which indicates there are no plans to implement this feature unfortunately (https://github.com/coreybutler/nvm-windows/issues/128)

### Local

For local development you want both the backend and frontend running for maximum efficiency :)

### To build and run the backend:

```bash
# Move to the backend directory
cd monolithstarter-svc

# Start the application in dev mode
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

You will now have a host running at [localhost:8080](http://localhost:8080/)

### To build and run the front-end:

```bash
# Move to the right directory
cd monolithstarter-static

# Install dependencies
yarn install

# Start the application in dev mode
yarn run start
```
You will now have your frontend running at [localhost:9000](http://localhost:9000/). Note that this won't work without the backend running!

To build frontend for production with minification:

```bash
yarn run build
```

### Production Build

```bash
# Install dependencies
yarn install

# run build script
yarn run build
```


### Docker
```bash
# Move to the monolithstarter-svc directory
cd monolithstarter-svc

# Generate a clean package
./mvnw clean package

# Construct the docker image
docker build -t monolithstarter-svc .

# Run the image container (substitute 5000 for desired port)
docker run -p 5000:8080 monolithstarter-svc
```
The full application is now accessible at [localhost:5000](http://localhost:5000/) (or whatever port you chose to use)
