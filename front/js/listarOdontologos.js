const apiURL = "http://localhost:8080";

// Obtener la referencia a la tabla y al modal
const tableBody = document.querySelector("#odontologoTable tbody");
const editModal = new bootstrap.Modal(document.getElementById("editModal"));
const editForm = document.getElementById("editForm");
let currentOdontologoId;

// Función para obtener y mostrar los odontólogos
function fetchOdontologos() {
  // listar los odontólogos
  fetch(`${apiURL}/odontologo/buscartodos`)
    .then((response) => response.json())
    .then((data) => {
      console.log(data);
      // Limpiar el contenido actual de la tabla
      tableBody.innerHTML = "";

      // Insertar los datos en la tabla
      data.forEach((odontologo, index) => {
        const row = document.createElement("tr");

        row.innerHTML = `
              <td>${odontologo.id}</td>
              <td>${odontologo.nroMatricula}</td>
              <td>${odontologo.apellido}</td>
              <td>${odontologo.nombre}</td>

              <td>
                <button class="btn btn-primary btn-sm" onclick="editOdontologo(${odontologo.id}, '${odontologo.nroMatricula}',
                 '${odontologo.apellido}', '${odontologo.nombre}')">Modificar</button>
                <button class="btn btn-danger btn-sm" onclick="deleteOdontologo(${odontologo.id})">Eliminar</button>
              </td>
            `;

        tableBody.appendChild(row);
      });
    })
    .catch((error) => {
      console.error("Error fetching data:", error);
    });
}

// Función para abrir el modal y cargar los datos del odontologo
editOdontologo = function (
  id,
  nroMatricula,
  apellido,
  nombre
) {
  currentOdontologoId = id;
  document.getElementById("editMatricula").value = nroMatricula;
  document.getElementById("editNombre").value = nombre;
  document.getElementById("editApellido").value = apellido;
  editModal.show();
};

// Función para editar un odontologo
editForm.addEventListener("submit", function (event) {
  event.preventDefault();
  const nroMatricula = document.getElementById("editMatricula").value;
  const nombre = document.getElementById("editNombre").value;
  const apellido = document.getElementById("editApellido").value;


  //modificar un odontologo
  fetch(`${apiURL}/odontologo/modificar`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      id: currentOdontologoId,
      nroMatricula,
      apellido,
      nombre,
    }),
  })
    .then((response) => response.json())
    .then((data) => {
      console.log(data);
      alert("Odontólogo modificado con éxito");
      fetchOdontologos();
      editModal.hide();
    })
    .catch((error) => {
      console.error("Error editando odontólogo:", error);
    });
});

// Función para eliminar un odontologo
deleteOdontologo = function (id) {
  if (confirm("¿Está seguro de que desea eliminar este odontólogo?")) {
    // eliminar el paciente
    fetch(`${apiURL}/odontologo/eliminar/${id}`, {
      method: "DELETE",
    })
      .then((response) => response.json())
      .then((data) => {
        console.log(data);
        alert("Odontólogo eliminado con éxito");
        fetchOdontologos();
      })
      .catch((error) => {
        console.error("Error borrando odontólogo:", error);
      });
  }
};

// Llamar a la función para obtener y mostrar los odontólogos
fetchOdontologos();
