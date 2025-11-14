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