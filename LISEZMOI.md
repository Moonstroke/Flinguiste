# Flinguiste

> Application mobile (Android uniquement, pour l'instant ?) de culture langagière : Vocabulaire, expressions (à venir : argot, et plus !)

Cette application n'est pas disponible sur le *Google Play Store*, mais le sera peut-être un jour ; toujours est-il que pour le moment, le seul moyen d'en profiter est de cloner ce dépôt et de compiler depuis [Android Studio](https://developer.android.com/studio/index.html).

## Présentation du jeu

Les fonctionnalités actuelles principale sont les questionnaires de langage :
- **Vocabulaire**
- **Expressions**

Chaque questionnaire présente par défaut 10<sup>[1](#note-1)</sup> mots, ou expressions ;
chaque question propose par défaut 4<sup>[1](#note-1)</sup> propositions de définition.

### Déroulement de la partie

La fenêtre principale de l'application propose à l'utilisateur de sélectionner le type de jeu qu'il désire
(*« Vocabulaire »* ou *« Expressions »*). Avant de lancer le jeu l'utilisateur peut choisir un niveau de jeu,
dans deux listes déroulantes (une par type de jeu) ou aucun (*« Aléatoire »*).
Quand l'utilisateur lance une partie, il se voit proposé un mot ou une expression, selon le type de jeu choisi,
et `n` propositions de définitions, le principe étant de sélectionner la définition correcte associée au mot,
ou à l'expression.

Si la réponse sélectionnée est correcte, le texte de la propostion s'affiche en vert, sinon en rouge ;
quoi qu'il en soit le bouton pour passer à la question suivante s'active.

Après avoir répondu aux `n` questions, est présenté à l'utilisateur un récapitulatif de sa partie,
avec le score global et la liste des mots ou expressions rencontrés.

### Paramètres

Il existe également une interface de Paramètres, où l'utilisateur peut modifier les valeurs par défaut pour le nombre de questions par partie,
et le nombre de propositions par question. Depuis cette interface on peut également interagir avec la base de données (ajouter un mot avec sa nature, son niveau, et sa définition, ou ajouter une définition *« bidon »*)

### Détails techniques

Les données de jeu (mots, expressions et définitions) sont stockés dans une base de données *SQLite*.

Des définitions dans la base de données peuvent n'être associées à aucun mot ou expression ; dans ce cas-là la valeur de cette colonne sera `NULL` dans la base de données.
Ces définitions sont alors des définitions *« bidon »* qui sont là pour renflouer la base de données, ou ajouter des petites références ou clins d'œil.

Les mots sont catégorisés selon leur **nature** grammaticale :
- substantif (*nom commun*)
- adjectif
- verbe

Les questions sont choisies aléatoirement<sup>[2](#note-2)</sup> dans la basse de données, sans doublon ;
les propositions sont choisies aléatoirement parmi toutes les définitions dont le mot est la valeur *SQLite* `NULL` ou la nature du mot est la même que le mot en exergue.


## À venir

- Ajouter le nouveau type de jeu : l'**argot** !

- Rajouter dans le récapitulatif de partie une mise en valeur qui indique dans la liste des mots rencontrés, auxquels on a répondu correctement

- Quand la base de données commencera à être remplie, et l'appli publiée sur le *Store*, rajouter un moyen, si l'appareil est connecté à Internet, de récupérer les dernières entrées en date (serveur *REST* ? fichier inclus dans ce dépôt ?)



## Notes et références

<a name="note-1">1</a> : valeur modifiable dans les **Paramètres**
<a name="note-2">2</a> : grâce à la fonction `RANDOM()` intégrée à *SQLite*
