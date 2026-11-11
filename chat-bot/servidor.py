from fastapi import FastAPI
from pydantic import BaseModel
from chatbot import perguntar, criar_contexto_cliente

app = FastAPI()

class ChatBotPergunta(BaseModel):
    nome: str
    cpf: str
    email: str
    pergunta: str

@app.get("/")
def inicio():
    return {
        "mensagem": "O chat está funcionando!"
    }

@app.post("/chat")
def conversar(dados: ChatBotPergunta):
    
    contexto = criar_contexto_cliente(dados.cpf, dados.email)
    
    resposta = perguntar(dados.pergunta, contexto)
    
    return {
        "resposta": resposta
    }
