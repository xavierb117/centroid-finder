# Human Plan!
- Find code to fix with human eyes

## Refactoring Code
- The classes we created doesn't have interfaces, would be a good idea to make iterfaces for the 3 of them
- Not as important, but in the server there may be an exception response that can be removed checking the size of the CSV. It could also be changed if needed, but low priority. 

## Adding Tests
- Server side may need more tests for responses
- May need more tests for routes that need more than 200 and 500 responses on Server (process/:filename and process/:jobId/status)
- Can split tests up into multiple files instead of 1 file

## Improving Error Handling
- When user types in bad input for target color on process/:filename, there already is error handling for this if enough time passes, but it could even be better if the input doesn't match exactly
- In our Java files, we just print the stack for the errors instead of actually logging them specifically. This can be improved by actually specifying what went wrong, what was thrown/caught.

## Writing Documentation
- Every new method we wrote from scratch is missing documentation.
- Java needs JavaDoc
- Server needs JSdoc

## Improving Performance (optional)
- 

## Hardening Security (optional)
-

## Bug Fixes (optional)
-

## Other (optional)
- Low priority, but maybe write a timer for files in archive and delete them after so archive doesn't get bloated.