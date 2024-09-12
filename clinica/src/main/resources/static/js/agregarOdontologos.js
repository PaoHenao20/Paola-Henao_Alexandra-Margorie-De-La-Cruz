const form = document.getElementById("agregarForm");
const apiURL = "http://localhost:8080";

form.addEventListener("submit", async function (event) {
  event.preventDefault();

  const apellido = document.getElementById("apellido").value;
  const nombre = document.getElementById("nombre").value;
  const matricula = document.getElementById("matricula").value;

  // llamando al endpoint de agregar
  const datosFormulario = {
    matricula,
    nombre,
    apellido
  };

  try{
    const response = await fetch(`${apiURL}/odontologo/guardar`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(datosFormulario),
      });
      const formatedResponse = await response.json();

       if (
            formatedResponse.statusCode === 400 ||
            formatedResponse.statusCode === 404
          ) {
            alert("Algo salió mal");
          } else {
            alert("El odontologo fue creado con exito");

          }
  form.reset(); // Resetear el formulario


  }catch(error){
    alert("Algo salió mal" + error)
  }
});