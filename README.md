# EasyMow
Simulation de tondeuses écrite en Scala

## Prérequis

* Scala 2.12
* sbt

## Compilation

Il suffit d'ouvrir un terminal dans le répertoire contenant le fichier *build.sbt*
et d'entrer la commande *sbt compile*.

## Exécution

Il vous faut entrer la commande *sbt run*. Il y a ensuite deux cas de figures:

* L'utilisateur peut fournir le chemin d'un fichier décrivant le jardin ainsi que
les tondeuses.
* Si aucun argument n'est fourni par l'utilisateur, le fichier *example.txt* situé
dans le répertoire *resources* sera utilisé.


## Choix d'implémentation

### Package model
-------------------

Dans ce package, nous allons définir les différents objets qui seront manipulés 
au cours de notre programme.

#### Coordinate

Cette classe est constituée de deux champs entiers et nous permettra de représenter
des coordonnées, elle sera utilisée notamment pour indiquer la position d'une 
tondeuse ou encore la taille d'un jardin.

#### Direction

Cette classe définit un trait __*Direction*__ permettant de représenter une direction
cardinale, ce dernier est étendu par les 4 objets suivants: 
* North
* East
* South
* West

Le trait définit deux méthodes __*nextFromRight*__ et __*nextFromLeft*__ qui seront
implantées par ces objets et renverront respectivement la prochaine direction à
droite et la prochaine à gauche.
Nous avons également créé un objet __*Direction*__ et redéfini la méthode __*apply*__ afin de 
pouvoir obtenir une direction cardinale à partir d'une chaîne de caractères (cela 
nous sera utile lorsque nous nous parserons le fichier de description). Dans le cas
où la chaîne ne correspond à aucune direction, nous renvoyons *None*.


#### Mower

La représentation d'une tondeuse dans notre programme, ses champs sont:
* Un objet Coordinate correspondant à la position de la tondeuse.
* Un objet Direction indiquant la direction cardinale à laquelle la tondeuse fait face.

Cette classe définit un certain nombre de méthodes permettant de mettre à jour la position ainsi que
la direction:

* __*rotateRight*__ et __*rotateLeft*__ modifient la direction à laquelle la tondeuse
fait face.
* __*advance*__ permet de faire avancer la tondeuse sur le jardin  d'une case dans
la direction à laquelle elle fait face. La tondeuse avance à condition que la case soit
valide c'est-à-dire qu'elle doit être à l'intérieur du jardin et ne pas être occupée
par une autre tondeuse.
* __*update*__ reçoit une chaîne de caractères et effectue l'action (tourner ou 
avancer) correspondante. Cette méthode sera utile lorsque nous parserons le fichier
de description, elle reconnaît donc les actions "D", "G" et "A". Si une autre chaîne
est passée en argument, aucune action n'est entreprise.

Nous avons créé un Companion Object définissant une méthode __*processUpdate*__ qui se chargera
de traiter une liste de chaînes de caractères et d'effectuer les actions correspondantes sur une tondeuse.

#### Garden

Cette classe nous permet de représenter un jardin dans notre programme, elle compte deux champs:
* un object Coordinate correspondant aux coordonnées du coin supérieur droit du jardin
* une liste de tondeuses

Elle définit plusieurs méthodes:
* __*isValidCoordinate*__ prend en paramètre un objet Coordinate et indique s'il
s'agit d'une position correcte (à l'intérieur du jardin et non occupée par une tondeuse)
* __*addMower*__ prend en paramètre un *Either* contenant soit une chaîne de caractères
décrivant une erreur, soit une tondeuse. S'il y a eu un problème, la méthode affiche
un message de log sinon elle ajoute la tondeuse à la liste.


### Package parser
--------------------

Ce package va nous offrir différentes méthodes permettant de parser des listes de 
chaînes de caractères afin de créer des jardins ou des tondeuses et d'effectuer des
actions sur nos tondeuses.

#### Parser

Nous définissons ici plusieurs méthodes traitant des listes de chaînes:
* __*parseGarden*__ recevra une liste correspondant à la première ligne du fichier
de description et construira le jardin dans lequel les tondeuses se déplaceront.
Si un problème se produit (si la liste ne contient pas assez d'éléments ou que l'un
de ses éléments ne correspond pas à un entier), elle renverra *None*.
* __*parseMower*__ recevra une liste correspondant à la ligne permettant d'initialiser
 une tondeuse. Si un problème se produit (la liste est incomplète ou ses éléments
 ne correspondent pas à un couple d'entiers et à une direction), elle renverra *None*.
* __*processMower*__ recevra deux listes: la première correspondra aux paramètres d'initialisation 
 d'une tondeuse, la seconde aux actions à exécuter. Si une erreur se produit lors de
 l'initialisation, un *Left* décrivant le problème sera renvoyé sinon, on renverra
 la tondeuse mise à jour.



### Package typeclass

Un package dédié à contenir les différents typeclasses implémentés.

#### Print

Ce typeclasse sera utilisé pour l'affichage en console de nos objets. Nous créons un
trait __*Show*__ définissant une méthode __*show*__ générique et renvoyant une chaîne de caractères.
Nous définissons ensuite pour chacun de nos objets une valeur implicite permettant de prouver que nos
objets implantent bien le typeclasse.
Une méthode générique __*print*__ est par la suite définie et prend en paramètre une preuve
 implicite de l'implantion du typeclasse pour renvoyer la chaîne de caractères correspondante. 

### Package draw
-----------------

Il s'agit d'un package contenant des fonctions de dessin sur une interface graphique.
Elle permet entre autre de dessiner un jardin (sous la forme d'un quadrillage) ainsi
 que les tondeuses qu'il contient. Ces dernières sont représentées par des cercles rouges,
 une flèche noire indiquant la direction à laquelle elles font face.
 
Les dessins ont été réalisés à l'aide des librairies java.awt et javax.swing.
 
Étant donné que le but de ce projet consistait plutôt à mettre au point une application
Scala appliquant les principes de la programmation fonctionnelle, cette interface graphique
n'était pas vraiment l'objet du sujet, c'est pourquoi elle n'est pas très poussée.
    La taille maximale de la fenêtre est fixée (900 * 900), la taille d'une case est alors
déterminée en fonction de la largeur ou de la hauteur du jardin (on prend le maximum des deux).

### Logger
-------------

Pour mes messages de log, j'ai eu recours à la librairie *Grizzled-SLF4J* qui est un wrapper du 
logger java *SLF4J* plus "scala-friendly". On a accès aux fonctionnalités de base telles que 
les messages d'information, d'avertissement ou même d'erreur.

### Tests
-----------

J'ai réalisé différents tests unitaires pour mes méthodes en utilisant en partie
les librairies *Scalatest* et *Scalacheck*. J'ai tenté au mieux d'avoir recours
aux différentes fonctionnalités offertes par ces deux librairires telles que les matchers, 
les générateurs, etc...






