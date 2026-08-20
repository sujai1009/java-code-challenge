Devices API
Your task is to develop a REST API capable of persisting and managing device resources.
Device Domain
• Name
• Brand
• State (available, in-use, inactive)
• Creation time
Supported Functionalities
• Create a new device.
• Fully and/or partially update an existing device.
• Fetch a single device.
• Fetch all devices.
• Fetch devices by brand.
• Fetch devices by state.
• Delete a single device.
Domain Validations
• Creation time cannot be updated.
• Name and brand properties cannot be updated if the device is in use.
• In use devices cannot be deleted.
Acceptance Criteria
• The application should compile and run successfully.
• The application must contain a reasonable test coverage.
• The API must be documented.
• The application must be capable of persisting resources on a database of your choice,
excluding in-memory.
• The application must be containerized.
• The project must be delivered as a git repository.
• The project includes a README file with all project related/necessary
documentation/instructions.
Requirements
• Java 21+

Maven 3.9+ or Gradle 8+
Tips
• Make sure to read all requirements, criteria and evaluation items before starting.
• Try to provide granular changes (e.g. commits) with comments and explanations on what is
being changed.
• Provide additional comments of future improvements and possible missing/malfunctioning
parts of your implementation.
• If you have questions or any additional information is required, don't hesitate to ask us!
Evaluation
The solution will be evaluated as per the following criteria:
• Implementation of all acceptance criteria.
• Usage of well-known best practices and design patterns.
• Code efficiency and general performance.
• Additional features and production readiness of the overall solution.
Final Remarks
• The project can either be delivered directly via email (zip file), or any remote repository
provider such as GitHub, GitLab, etc. Just remember the deliverable must be a git repository.
