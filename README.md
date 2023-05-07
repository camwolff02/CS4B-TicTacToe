1. separate different projects
    A. Router Project
        - ClientHandler
        - Router
    B. UI Project
        - JavaFX UI (refactored)
        - Online logic class/ Client (refactored)
    C. Game Manager
        - PlayerManager / Client (refactored)
        - BoardController

2. Make program PlayerManager and BoardController
    Program PlayerManager:
        - after starting, connects to router
        - subscribes to some channel to see new player info
        - spins up BoardControllers when it sees "start game message" over channel
        - puts players in BoardControllers
    Program BoardController:
        - run in its own thread
        - manages 2 players 
        - controls game logic for 2 players
        - sends messages to players about game logic
        - lets 2 players play a game together 

===============================================================================

NOTE: login system is low priority, would be useful for saving data (possibly 
SQL, JSON, or CSV) and leaderboard

NOTE: stretch goal, have AI take over if player disconnects
NOTE: stretch goal, ingame chat, EMOTES???

NOTE: if we need unique names and pfp on board, can remove pfp from login, 
have screen to select "display name" (different from username) and pfp

QUESTION: should we have user select username and profile picture after both are
connected to make sure username and pfp are unique?



MESSAGES
CreateLogin = username, password, profile picure
AddProfilePic = profile picture (used if user uploads custom pfp)
ClientInfo = username, profile picture
Login = username, password
CreateGame = Lobby Name
JoinGame = Lobby Name
MakeMove = move position
ListGames = <empty>
ListOfGames = [lobby names of non-full games]
ActionSuccess = True or False 
StartGame = <empty>
ClientDisconnected = <empty>
GameOver = Win, Lose, Tie
PlayAgain = True or False
Exit = <empty>



NOTE: DON'T have a generic action success for every message response
- have a base class for request and for response
- maybe break up message into chunks, like so:
    | message type | client sending/receiving | data | etc. |

MESSAGE EXAMPLES
client (login): CreateLogin -> server
server: ActionSuccess -> client (fails if username in use)

client (login): AddProfilePic -> server
server: ActionSuccess -> client

client (login): Login -> server
server: ActionSuccess -> client (put in menu)

client (menu): CreateGame -> server
server: ActionSuccess -> client (put in lobby)

client (menu): ListGames -> server
server: ListOfGames -> client (displayed to menu)

client (menu): JoinGame -> server
server: ClientInfo -> client (put in lobby, sent info of client they just joined)

server (lobby): ClientInfo -> client (sends info of client who joined lobby to client already in lobby)

client (lobby): StartGame -> server
server: ActionSuccess -> both clients (tell client GUIs to display game IF action success)

client (game screen): MakeMove -> server
server: BoardState -> client

server (game screen): GameOver -> client

server (any): ClientDisconnected -> client

client (game screen or loby) Exit -> server
server: ActionSuccess -> client (booted to previous screen, send ClientDisconnected to other client if in game)

client (game screen): PlayAgain -> server
server: ActionSuccess -> client (restart game IF both players selected play again)