# PhotoAlbum
[Kanban Board](https://trello.com/b/ZE4HeJ8Q/mainboard)

## Setup
1. Enable annotation processing in IntelliJ:
 File -> Settings -> Build, Execution, Deployment -> Compiler -> Annotation Proccessors -> Check "Enable annotation processing"
2. Change username, password under resources/application.yml
3. resources/schema.sql contains scripts for creating db
4. Install Lombok plugin
5. Create folder C:\\photoAlbum (for storing uploaded photos)

## Mentions
* Use @Slf4J on any class where you want to use logging.

## Post-Migration
1. move .idea, target folders to Backend
2. make sure there are no leftovers in root folder
3. reopen Backend folder in Intellij -> There should be a "VSC" error -> press on configure -> remove "/Backend" folder