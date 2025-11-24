# AI Improvements
- Improvements that AI notices

## Refactoring Code
- DFSBinaryGroupFinder could utilize a helper function to better check for invalid inputs / validate the image
- AI: Reduce repeated array validation + simplify rectangle checks with one dedicated validator method

## Adding Tests
- DistanceImageBinarizerTest could possibly be tested for negative and larger values to test full functionality
- AI: DistanceImageBinarizer lacks negative-case testing for invalid thresholds, corrupted images, and bad pixel values, which leaves important failure paths unverified.

## Improving Error Handling
- processController could use more detailed error codes, giving the user more of an idea of what they need to do correctly
- AI: Give meaningful error messages that explain why the failure happened

## Writing Documentation
- We could add a short paragraph explaning each file and what it is doing
- AI: Add a top-level comment block to each file explaining its purpose

## Improving Performance (optional)
- EuclideanColorDistance using Math.sqrt and Math.pow is expensive for processing, we could do a different algorithmn to process the distance between, to improve performance.
- AI: The Euclidean color distance calculation can be optimized by removing slow sqrt and pow operations and comparing squared distances instead, greatly improving per-pixel performance

## Hardening Security (optional)
- CORS opens vunerability for anyone to make requests to our server, we could fix this by only allowing our route to access it
- AI: CORS should be restricted to known front-end origins; allowing all origins in a containerized API can expose the server to unauthorized cross-site requests
- app.use(cors({
  origin: ["http://localhost:3001", "https://yourfrontend.com"]
}));

## Bug Fixes (optional)
- 

## Other (optional)
- 