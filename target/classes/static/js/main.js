// Obtener el input de búsqueda
const searchInput = document.getElementById('search');
const searchButton = document.getElementById('searchButton');
const pokemonList = document.getElementById('pokemonList');
const pagination = document.querySelector('.pagination');

// Función para cargar todos los Pokémon
function loadAllPokemons() {
    fetch('/pokedex/')
        .then(response => response.text())
        .then(html => {
            let parser = new DOMParser();
            let doc = parser.parseFromString(html, 'text/html');
            let newList = doc.querySelector('#pokemonList');

            if (newList) {
                document.querySelector('#pokemonList').innerHTML = newList.innerHTML;
            } else {
                console.error("No se encontró el contenedor de Pokémon en la respuesta HTML.");
            }
        })
        .catch(error => console.error('Error al cargar todos los Pokémon:', error));
}

// Escuchar el evento 'input' en el campo de búsqueda
searchInput.addEventListener('input', function () {
    let query = this.value.trim();

    // Si el campo está vacío, cargamos todos los Pokémon
    if (query.length === 0) {
        loadAllPokemons();
    } else if (query.length >= 3) {
        pagination.classList.add('hidden');
        fetch(`/pokedex/search?query=${query}`)
            .then(response => response.text())
            .then(html => {
                let parser = new DOMParser();
                let doc = parser.parseFromString(html, 'text/html');
                let newList = doc.querySelector('#pokemonList');

                if (newList) {
                    document.querySelector('#pokemonList').innerHTML = newList.innerHTML;
                } else {
                    console.error("No se encontró el contenedor de Pokémon en la respuesta HTML.");
                }
            })
            .catch(error => console.error('Error al buscar:', error));
    }
});

// Escuchar el evento 'click' en el botón de búsqueda
searchButton.addEventListener('click', function () {
    let query = searchInput.value.trim();
    console.log("Query a enviar: ", query);
    if (query.length >= 3) {
        pagination.classList.add('hidden');
        fetch(`/pokedex/search?query=${query}`)
            .then(response => response.text())
            .then(html => {
                let parser = new DOMParser();
                let doc = parser.parseFromString(html, 'text/html');
                let newList = doc.querySelector('#pokemonList');  // Seleccionamos la nueva lista

                if (newList) {
                    document.querySelector('#pokemonList').innerHTML = newList.innerHTML;
                } else {
                    console.error("No se encontró el contenedor de Pokémon en la respuesta HTML.");
                }
            })
            .catch(error => console.error('Error al buscar:', error));
    }
});

// Escuchar el evento 'input' para mostrar todos los pokemons si no hay búsqueda
searchInput.addEventListener('input', function () {
    let query = this.value.trim();
    if (query.length < 3) {
        pagination.classList.remove('hidden');  // Mostramos la paginación cuando no haya búsqueda
    }
});

// Función para manejar el desplazamiento hacia abajo
document.getElementById("scrollToBottom").addEventListener("click", function () {
    window.scrollTo({top: document.body.scrollHeight, behavior: "smooth"});
});
