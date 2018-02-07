# queue-transaction

Aplicativo que recebe como entrada (stdin) uma lista de transações (uma por linha) e envia o dado para uma fila ActiveMQ

## Uso

```
 java -jar  queue-transaction.jar -u login:senha http://host/api/message/queuename?type=queue < arquivocomtransacoes.txt
```