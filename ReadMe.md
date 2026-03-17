https://github.com/Akseben/Assignment5_Playwright/actions/workflows/Playwright.yml/badge.svg
https://github.com/Akseben/Assignment5_Playwright.git

## Project Overview
This project introduces the Playwright toolset, exploring its capabilities using it both manually and with AI agents controlling it. The two methods for developing unit testing are then compared for their efficiency and usability.

## Link to workflow: 
[.github\workflows\Playwright.yml]

## All actions passed in Git Actions

### Essay
In assignment 5, we were tasked with creating unit tests utilizing Microsoft’s Playwright, a software that can take inputs from a browser and turn them into replicable unit tests. For the first part of this assignment, we did this by running playwright manually, taking the code it gave, and putting it in its own file, separating each section into its own test. For the second part, we were tasked to use a Playwright MCP server with an AI agent and prompt said agent to create the tests for us.
For the first part of Playwright, there were several pros and cons to be considered. To start, since the system was being implemented by hand, I had a much better grasp of what was going on, therefore was able to debug problems far quicker. On top of that, I was also able to gain a lot of knowledge and understanding of how Playwright worked, and as I went through the assignment was able to more quickly write code and debug problems. There was also far more consistency in how long it would take to complete this part of the assignment. Since I fully understood the code and playwright made the tests bulletproof, the process was streamlined. For the drawbacks of this approach, I found them to be quite minimal. It did take a bit more manual effort, but it was worth it in the end as it resulted in clean code and successful tests after very few attempts.
For the second part of Playwright, I found there to be far more cons than pros. Although the AI did all the heavy lifting for writing the tests and analyzing the website to create the flow, it ran into many errors. When I prompted the AI to fix the errors, it would do so, but the result would almost always result in more errors. I would frequently see the errors repeated over several renditions of the test file and needed to intervene, giving it advice based on how I did my testing for it to pass certain bottlenecks. Another issue with the process was the uncertainty, although I didn’t have to troubleshoot the issues myself, I wasn’t sure of when the AI would be able to fix everything and create a product that would pass the maven tests. This unreliability makes me uneasy utilizing AI for such large, hands-off tasks where it must create code all by itself with minimal supervision. 
Overall, I would say that the manual testing was far easier than utilizing AI. In terms of writing and running tests, it was far cleaner, and the accuracy of the tests themselves was far greater when running Playwright on my own. Granted, the AI was able to fix the mistakes on its own without much human intervention, unlike when I had errors manually coding the tests. 
