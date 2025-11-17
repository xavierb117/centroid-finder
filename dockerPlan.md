## Docker File Plan

### Ubuntu BASE-IMAGE
#### Pros:
- Ubuntu installs FFMPEG from its APT (Advanced Packaging Tool)
#### Cons:
- Need to install node 
- Need to install JRE (Java Runtime Enviorment)
- Must install everything yourself manually
### Node BASE-IMAGE
#### Pros:
- Able to avoid Node installation as the whole image falls under the Node.js Framework
- Java JAR can run here as well, recommends JDK
#### Cons: 
- Need to install FFMPEG
- Need to install JRE (Java Runtime Enviorment)
### File Structure/Layout
- Create a working (App) file to act as the root **WORKDIR /app**
- Create a Server File for Server logic **WORKDIR /app/server**
- Create Enviorment Variables (ENV) to require Videos and Results directory **ENV VIDEO_DIRECTORY, ENV RESULTS_DIRECTORY**
- We will need to create **Holder** Files just so docker knows where to look, the user will need to input -v along with their directory addresses when running the image
- Will need to copy Package.json and then the Server Routes from Local Server directory to Images Server Directory 
- Will need to copy our JAR from Local to Image Root
- WIll need to run on port 3000 for making endpoints available
- Will need to run a command (CMD) for npm i
- Run File 
### Choices
- Listed a few pros and cons for each of the base images above, the content of Dockerfile depends on each
- Ubuntu sounds more technical and manual but could be powerful
- Node has many base images, so far some recommended ones are node:20-bookworm and node:18-bullseye.
- Some Node images can already be eliminated, such as Node alpine or Node slim because they don't work with ffmpeg as well.
- May go for Node Base Image. Ubuntu may have ffmpeg, but it seems to be more technical, manual and bigger. Node may be more simple, as it has Node installed, ffmpeg and Java can be easily installed, and it could be more easier to maintian with less manual work. 