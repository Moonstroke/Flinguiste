DELETE FROM Definition;
DELETE FROM Entree;
DELETE FROM Nature;
DELETE FROM Type;
DELETE FROM Niveau;


.shell echo "niveau"

INSERT INTO Niveau (id_niv, niveau) VALUES
	(0, 'Aléatoire'); -- pour sélectionner des mots de n'importe quel niveau

INSERT INTO Niveau (niveau) VALUES
	('Cultivé'),
	('Connaisseur'),
	('Omniscient');


.shell echo "type"

INSERT INTO Type (type) VALUES
	('Mot'),
	('Expression');


.shell echo "nature"

INSERT INTO Nature (id_nat, id_type, nature) VALUES
	(1, 1, 'substantif'),
	(1, 2, 'périphrase'),
	(2, 1, 'adjectif'),
	(2, 2, 'métaphore'),
	(3, 1, 'verbe'),
	(3, 2, 'locution');


.shell echo "entree"

INSERT INTO Entree (entree, id_type, id_nat, id_niv) VALUES
	('acabit', 1, 1, 1),
	('acerbe', 1, 2, 1),
	('burlesque', 1, 2, 1),
	('cadastre', 1, 1, 1),
	('céphalée', 1, 1, 1),
	('coquille', 1, 1, 1),
	('courroux', 1, 2, 1),
	('dérisoire', 1, 2, 1),
	('désarroi', 1, 1, 1),
	('diapason', 1, 1, 1),
	('draconien', 1, 2, 1),
	('drastique', 1, 2, 1),
	('énergumène', 1, 1, 1),
	('équipollent', 1, 2, 1),
	('fastidieux', 1, 2, 1),
	('friche', 1, 1, 1),
	('grotesque', 1, 2, 1),
	('hirsute', 1, 2, 1),
	('incongru', 1, 2, 1),
	('insatiable', 1, 2, 1),
	('ladre', 1, 1, 1),
	('moyeu', 1, 1, 1),
	('nyctalope', 1, 2, 1),
	('obnubiler', 1, 3, 1),
	('oligarchie', 1, 1, 1),
	('opercule', 1, 1, 1),
	('ornière', 1, 1, 1),
	('panacée', 1, 1, 1),
	('péremptoire', 1, 2, 1),
	('péricliter', 1, 3, 1),
	('plébisciter', 1, 3, 1),
	('polyglotte', 1, 2, 1),
	('pompeux', 1, 2, 1),
	('postulat', 1, 1, 1),
	('préconiser', 1, 3, 1),
	('ramequin', 1, 1, 1),
	('rébarbatif', 1, 2, 1),
	('recrudescence', 1, 1, 1),
	('redevable', 1, 2, 1),
	('réquisitoire', 1, 1, 1),
	('rétorquer', 1, 3, 1),
	('s''insurger', 1, 3, 1),
	('sédition', 1, 1, 1),
	('stochastique', 1, 2, 1),
	('subversif', 1, 2, 1),
	('véhémence', 1, 1, 1),
	('vestiblue', 1, 1, 1),
	('vétuste', 1, 2, 1), -- ou vétusté ?
	('taciturne', 1, 2, 1),
	('tergiverser', 1, 3, 1),
	('transhumance', 1, 1, 1),
	('triptyque', 1, 1, 1),

	('alacrité', 1, 1, 2),
	('apanage', 1, 1, 2),
	('aphorisme', 1, 1, 2),
	('ascèse', 1, 1, 2),
	('astérisme', 1, 1, 2),
	('aune', 1, 1, 2),
	('babillage', 1, 1, 2),
	('badinage', 1, 1, 2),
	('céladon', 1, 2, 2),
	('connivence', 1, 1, 2),
	('corroborer', 1, 3, 2),
	('crécelle', 1, 1, 2),
	('déréliction', 1, 1, 2),
	('destitution', 1, 1, 2),
	('ébaubir', 1, 3, 2),
	('enter', 1, 3, 2),
	('entériner', 1, 3, 2),
	('évincer', 1, 3, 2),
	('exhorter', 1, 3, 2),
	('faste', 1, 1, 2),
	('gentilé', 1, 1, 2),
	('houspiller', 1, 3, 2),
	('idoine', 1, 2, 2),
	('impavide', 1, 2, 2),
	('indolent', 1, 2, 2),
	('meneau', 1, 1, 2),
	('miasme', 1, 1, 2),
	('mitoyen', 1, 2, 2),
	('oblitération', 1, 1, 2),
	('ortolan', 1, 1, 2),
	('parangon', 1, 1, 2),
	('pléthore', 1, 1, 2),
	('procrastiner', 1, 3, 2),
	('pugilat', 1, 1, 2),
	('ratifier', 1, 3, 2),
	('rédhibitoire', 1, 2, 2),
	('retable', 1, 1, 2),
	('s''esclaffer', 1, 3, 2),
	('solive', 1, 1, 2),
	('superfétatoire', 1, 2, 2),
	('tarauder', 1, 3, 2),
	('thaumaturge', 1, 2, 2),
	('truculent', 1, 2, 2),
	('turpitude', 1, 1, 2),
	('vindicatif', 1, 2, 2),

	('acmé', 1, 1, 3),
	('ambage', 1, 1, 3),
	('ataraxie', 1, 1, 2),
	('atavisme', 1, 1, 3), -- ou atavique ?
	('attrition', 1, 1, 3),
	('caillebotis', 1, 1, 3),
	('contingence', 1, 1, 3),
	('coquecigrue', 1, 1, 3),
	('cuniculiculture', 1, 1, 3),
	('diatribe', 1, 1, 3),
	('dityrambique', 1, 2, 3),
	('ébaudir', 1, 3, 3),
	('écornifleur', 1, 1, 3),
	('glossolalie', 1, 1, 3),
	('houppelande', 1, 1, 3),
	('icoglan', 1, 1, 3),
	('impost', 1, 1, 3),
	('indigent', 1, 2, 3),
	('innutrition', 1, 1, 3),
	('lambourde', 1, 1, 3),
	('maroufle', 1, 1, 3),
	('palinodie', 1, 1, 3),
	('panégyrique', 1, 2, 3),
	('pétrichor', 1, 1, 3),
	('phylactère', 1, 1, 3),
	('carteron', 1, 1, 3),
	('remugle', 1, 1, 3),
	('ripisilve', 1, 1, 3),
	('se morigéner', 1, 3, 3),
	('sérendipité', 1, 1, 3),
	('stipendié', 1, 2, 3),
	('sycophante', 1, 1, 3),
	('truchement', 1, 1, 3),
	('vertugadin', 1, 1, 3),
	('vilipender', 1, 3, 3),


	('aimable comme une porte de prison', 2, 2, 1),
	('avoir la tête dans le pâté', 2, 3, 1),
	('bayer aux corneilles', 2, 3, 1),
	('croiser le fer', 2, 3, 1),
	('du coq à l''âne', 2, 2, 1),
	('dur de la feuille', 2, 2, 1),
	('faire chou blanc', 2, 3, 1),
	('franchir le Rubicon', 2, 3, 1),
	('entre le marteau et l''enclume', 2, 2, 1),
	('haut comme trois pommes', 2, 2, 1),
	('jeter l''éponge', 2, 3, 1),
	('manger les pissenlits par la racine', 2, 3, 1),
	('mettre la charrue avant les bœufs', 2, 3, 1),
	('mettre la puce à l''oreille', 2, 3, 1),
	('ne pas faire long feu', 2, 3, 1),
	('noyer le poisson', 2, 3, 1),
	('peigner la girafe', 2, 3, 1),
	('pincer la bulle', 2, 3, 1),
	('retourner sa veste', 2, 3, 1),
	('sans queue ni tête', 2, 2, 1),
	('tenir la chandelle', 2, 3, 1),
	('tirer les vers du nez', 2, 3, 1),
	('tourner autour du pot', 2, 3, 1),

	('avoir le feu au plancher', 2, 3, 2),
	('avoir plus d''une corde à son arc', 2, 3, 2),
	('brûler la chandelle par les deux bouts', 2, 3, 2),
	('déshabiller Pierre pour habiller Paul', 2, 3, 2),
	('être sauvé par le gong', 2, 3, 2),
	('faire flèche de tout bois', 2, 3, 2),
	('faire long feu', 2, 3, 2),
	('feuille de chou', 2, 1, 2),
	('les bêtes de Panurge', 2, 1, 2),
	('ménager la chèvre et le chou', 2, 3, 2),
	('mener la truie au foin', 2, 3, 2),
	('pas piqué des hannetons', 2, 2, 2),
	('passer l''arme à gauche', 2, 3, 2),
	('prendre des vessies pour des lanternes', 2, 3, 2),
	('sur la sellette', 2, 2, 2),
	('tirer le diable par la queue', 2, 3, 2),
	('tomber de Charybde en Scylla', 2, 3, 2),
	('une volée de bois vert', 2, 1, 2),

	('coiffer Sainte Catherine', 2, 3, 3),
	('comme un emplâtre sur une jambe de bois', 2, 2, 3),
	('connaître mouche en lait', 2, 3, 3),
	('déménager à la cloche de bois', 2, 3, 3),
	('la lanterne rouge', 2, 1, 3),
	('monter au pinacle', 2, 3, 3),
	('s''en soucier comme Jean de Vert', 2, 3, 3),
	('suivre l''évangile des quenouilles', 2, 3, 3),
	('voir sa queue reluire', 2, 3, 3),
	('tirer des plans sur la comète', 2, 3, 3),
	('trancher le nœud gordien', 2, 3, 3)
;


.shell echo "definition"

INSERT INTO Definition (definition) VALUES
	('42'),
	('mauvaise réponse'),
	('la réponse D');
