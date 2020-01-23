const serverLocation = window.location.hostname

fetch(`http://${serverLocation}:8081/`).then(x => console.log(x))

const socket = new WebSocket(`ws://${serverLocation}:8081/wsapp/debugger`)

socket.addEventListener("open", event => {
  socket.send("hello server")
})

socket.addEventListener('message', event => {
  console.log(`message from server: ${event.data}`)
})
