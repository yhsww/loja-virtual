from fastapi import FastAPI, HTTPException
from pydantic import BaseModel, Field

from chatbot import criar_contexto_cliente, perguntar


app = FastAPI(
    title="Chatbot da Loja Virtual",
    description="API da assistente virtual Íris.",
    version="1.0.0",
)


class ChatBotPergunta(BaseModel):
    nome: str = Field(..., min_length=1)
    cpf: str = Field(..., min_length=1)
    email: str = Field(..., min_length=1)
    pergunta: str = Field(..., min_length=1)


@app.get("/")
def inicio():
    return {
        "mensagem": "O chat está funcionando!"
    }


@app.post("/chat")
def conversar(dados: ChatBotPergunta):
    try:
        contexto = criar_contexto_cliente(
            dados.cpf,
            dados.email,
        )

        resposta = perguntar(
            dados.pergunta,
            contexto,
        )

        return {
            "resposta": resposta
        }

    except ValueError as exc:
        raise HTTPException(
            status_code=400,
            detail=str(exc),
        ) from exc

    except RuntimeError as exc:
        raise HTTPException(
            status_code=500,
            detail=str(exc),
        ) from exc

    except Exception as exc:
        raise HTTPException(
            status_code=500,
            detail="Erro interno ao processar a pergunta.",
        ) from exc
