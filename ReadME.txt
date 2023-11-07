Le programme est concu pour envoyer des messages chiffrés en localhost d'un client vers un serveur.

Il faut run le serveur en premier, pour que cela fonctionne convenablement.
Une fois le serveur démarré, le client peut être démarré également, et s'en suit un échange de clés grâce a l'algorithme de Diffie-Hellmann

Le client propose alors de choisir entre chiffrer en 3DES ou en AES, puis demande d'encoder le texte.
Le token HMAC-MD5 est ajouté directement au message après encodage, puis le texte est chiffré.
Le texte chiffré est envoyé au serveur, et directement après, le client envoie également le hash SHA-1 du texte chiffré qu'il vient d'envoyer
Le serveur reçoit le texte, le déchiffre, compare les HMAC-MD5 et authentifie l'expéditeur. Si l'expéditeur est valide, il print le texte.
Le serveur reçoit ensuite le hash SHA-1, il calcule le hash SHA-1 du dernier message chiffré qu'il a reçu et il les compare. Si les hash sont identiques,
c'est que le message n'a pas été modifié, et le serveur print que le message est authentique.

