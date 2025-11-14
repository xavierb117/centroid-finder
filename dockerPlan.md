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
- Create a working (App) file to act as the root
- Create a Server File for Server logic
- Create Enviorment Variables (ENV) to require Videos and Results directory
- Will need to copy Package.json from Local Server directory to Images Server Directory
- Will need to copy our JAR from Local to Image Root
- Will need to run a command (CMD) for npm i
- Run File 