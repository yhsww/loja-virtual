import os
import json

from dotenv import load_dotenv 
from google import genai
 
load_dotenv() #carregar chaves do .env
api_key = os.getenv("GEMINI_API_KEY") #definir a chave específica do gemini

client = genai.Client(api_key=api_key) #passar para o cliente a chave escolhida do gemini

def carregar_produtos():
    caminho = "../dados/produtos.json"
    
    with open(caminho, "r", encoding="utf-8") as file:
        produtos = json.load(file)
        
    return produtos

def formatar_produtos(produtos):
    
    texto = "Produtos disponíveis\n\n"
    
    for produto in produtos:
        texto += (
            f" - {produto['nome']}\n"
            f"      - Preço: R${produto['preco']/100:.2f}\n"
            f"      - Estoque: {produto['estoque']} unidades\n\n"
        )
    
    return texto

def carregar_compras():
    caminho = "../dados/compras.json"
    
    with open(caminho, "r", encoding="utf-8") as file:
        compras =json.load(file)
        
    return compras

def carregar_compradores():
    caminho = "../dados/compradores.json"
    
    with open(caminho, "r", encoding="utf-8") as file:
        compradores=json.load(file)
        
    return compradores

compradores = carregar_compradores()
produtos = carregar_produtos()
compras = carregar_compras()
texto_produtos = formatar_produtos(produtos)


def obter_historico_cliente(compras, cpf_cliente, email_cliente):
    historico = []
    
    for compra in compras:
        if (
            compra.get("compradorCpf") == cpf_cliente
            and compra.get("compradorEmail") == email_cliente
        ):
            historico.append(
                    {
                    "Data": compra["dataHora"],
                    "Itens": compra["itens"],
                    "Subtotal": compra["subtotal"],
                    "Desconto": compra["desconto"],
                    "ValorFinal": compra["valorFinal"],
                    "Pagamento": compra["pagamento"],
                    "Cancelada": compra["cancelada"]
                }
            )
            
    return historico

def formatar_historico_cliente(historico):
    if not historico:
        return "O cliente ainda não possui histórico de compras."
    
    texto = "Histórico de compras do cliente\n\n"
    
    for compra in historico:
        
        texto += f" Data: {compra['Data']}\n"
        texto += " Itens:\n"

        for item in compra["Itens"]:
           
           preco = item["precoUnitario"]/100
           
           texto += (
               f" - Nome: {item['nome']} - Total de unidades: {item['qtd']}\n"
               f" - Preço unitário: R${preco:.2f}\n"
           )   

        subtotal = compra['Subtotal']/100
        desconto = compra['Desconto']/100
        valor_final = compra['ValorFinal']/100
            
        texto += (
            f" - Subtotal: R${subtotal:.2f}\n"
            f" - Desconto: R${desconto:.2f}\n"
            f" - Valor final: R${valor_final:.2f}\n"
        )
        
        if compra["Cancelada"]:
            texto += f"Status de compra: cancelada\n"
        else:
            texto += f"Status de compra: concluída"

        texto += "\n\n"
            
    return texto

def criar_contexto_cliente(cpf_cliente, email_cliente):

    historico = obter_historico_cliente(compras, cpf_cliente, email_cliente)

    texto_historico = formatar_historico_cliente(historico)
    
    contexto = f"""
        Você é a Íris, assistente virtual da loja.
        Use os produtos abaixo para responder às perguntas do cliente.
        {texto_produtos}
        {texto_historico}
        Não invente produtos, preços ou estoques.
    """
    
    return contexto

def perguntar(pergunta, contexto):
    
    resposta = client.models.generate_content(
        model="gemini-3.5-flash",
        contents=contexto + "\nPergunta do cliente: " + pergunta
    )
    
    return resposta.text