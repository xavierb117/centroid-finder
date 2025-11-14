## Docker File Plan

### Ubuntu BASE-IMAGE
#### Pros:
- Ubuntu installs FFMPEG from its APT (Advanced Packaging Tool)
#### Cons:
- Need to install node 
- Need to install JRE (Java Runtime Enviorment)
### Node BASE-IMAGE
#### Pros:
- Able to avoid Node installation as the whole image falls under the Node.js Framework
#### Cons: 
- Need to install FFMPEG
- Need to install JRE (Java Runtime Enviorment)
### File Structure/Layout
- Create a working (App) file to act as the root **WORKDIR /app**
- Create a Server File for Server logic **WORKDIR /app/server**
- Create Enviorment Variables (ENV) to require Videos and Results directory **ENV VIDEO_DIRECTORY, ENV RESULTS_DIRECTORY**
    - We will need to create **Holder** Files just so docker knows where to look, the user will need to input -v along with their directory addresses when running the image
- Will need to copy Package.json from Local Server directory to Images Server Directory 
- Will need to copy our JAR from Local to Image Root
- Will need to run a command (CMD) for npm i
- Run File 