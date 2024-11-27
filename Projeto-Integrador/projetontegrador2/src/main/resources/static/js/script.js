const API_URL = 'http://localhost:8080/zoo'; // Atualize com sua URL da API

document.getElementById("animal-form").addEventListener("submit", handleSubmit);

async function handleSubmit(event) {
    event.preventDefault();

    const id = document.getElementById("animal-id").value;
    const nome = document.getElementById("nome").value;
    const especie = document.getElementById("especie").value;
    const genero = document.getElementById("genero").value;
    const idade = document.getElementById("idade").value;
    const imagem = document.getElementById("imagem").files[0];

    const formData = new FormData();
    formData.append("nome", nome);
    formData.append("especie", especie);
    formData.append("genero", genero);
    formData.append("idade", idade);
    if (imagem) formData.append("imagem", imagem);

    const method = id ? "PUT" : "POST";
    const url = id ? `${API_URL}/${id}` : API_URL;

    try {
        const response = await fetch(url, {
            method,
            body: formData,
        });

        if (response.ok) {
            showMessage("Animal cadastrado com sucesso!", "success");
            document.getElementById("animal-form").reset();
            loadAnimals();
        } else {
            showMessage("Erro ao cadastrar o animal. Verifique os dados.", "error");
        }
    } catch (error) {
        console.error("Erro:", error);
        showMessage("Erro ao conectar com o servidor.", "error");
    }
}

function showMessage(message, type) {
    const mensagemDiv = document.getElementById("mensagem");
    mensagemDiv.textContent = message;
    mensagemDiv.style.color = type === "success" ? "green" : "red";
    setTimeout(() => (mensagemDiv.textContent = ""), 3000);
}

async function loadAnimals() {
    try {
        const response = await fetch(API_URL);
        const animals = await response.json();

        const animalList = document.getElementById("animal-list");
        animalList.innerHTML = ""; // Limpar lista anterior

        animals.forEach((animal) => {
            const li = document.createElement("li");
            li.textContent = `${animal.nome} - ${animal.especie}`;
            if (animal.imagem) {
                const img = document.createElement("img");
                img.src = `http://localhost:8080/${animal.imagem}`;
                img.alt = animal.nome;
                img.style.width = "100px";
                img.style.height = "100px";
                li.appendChild(img);
            }
            animalList.appendChild(li);
        });
    } catch (error) {
        console.error("Erro ao carregar a lista de animais:", error);
    }
}

// Carregar a lista de animais ao iniciar a página
loadAnimals();
