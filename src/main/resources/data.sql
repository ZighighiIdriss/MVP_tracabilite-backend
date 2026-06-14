-- ================================
-- Données initiales (seed)
-- ================================

-- ========== MODIFIÉ : Tables de référence (anciennes valeurs des enums) ==========

-- Types de produit (ancien enum ProductType : FLO, BIO, CAR, HUI)
INSERT INTO product_types (id, name) VALUES (1, 'FLO') ON DUPLICATE KEY UPDATE name=name;
INSERT INTO product_types (id, name) VALUES (2, 'BIO') ON DUPLICATE KEY UPDATE name=name;
INSERT INTO product_types (id, name) VALUES (3, 'CAR') ON DUPLICATE KEY UPDATE name=name;
INSERT INTO product_types (id, name) VALUES (4, 'HUI') ON DUPLICATE KEY UPDATE name=name;

-- Matières premières (ancien enum Material : ROS, LAV, ARG, MEN)
INSERT INTO materials (id, name) VALUES (1, 'ROS') ON DUPLICATE KEY UPDATE name=name;
INSERT INTO materials (id, name) VALUES (2, 'LAV') ON DUPLICATE KEY UPDATE name=name;
INSERT INTO materials (id, name) VALUES (3, 'ARG') ON DUPLICATE KEY UPDATE name=name;
INSERT INTO materials (id, name) VALUES (4, 'MEN') ON DUPLICATE KEY UPDATE name=name;

-- Statuts calqués sur la timeline industrielle
INSERT INTO statuses (id, name) VALUES (1, 'RECEPTION_MATIERE') ON DUPLICATE KEY UPDATE name=name;
INSERT INTO statuses (id, name) VALUES (2, 'TRANSFORMATION') ON DUPLICATE KEY UPDATE name=name;
INSERT INTO statuses (id, name) VALUES (3, 'PURIFICATION_EXTRACTION') ON DUPLICATE KEY UPDATE name=name;
INSERT INTO statuses (id, name) VALUES (4, 'TERMINE') ON DUPLICATE KEY UPDATE name=name;
INSERT INTO statuses (id, name) VALUES (5, 'ANNULE') ON DUPLICATE KEY UPDATE name=name;

-- ========== FIN des tables de référence ==========

-- Fournisseurs
INSERT INTO suppliers (id, supplier_code, name)
VALUES (1, 'FOU-OU-2026-01', 'Fournisseur Arômes Casablanca')
ON DUPLICATE KEY UPDATE name=name;

-- Utilisateur démo (pour le mode démo)
INSERT INTO users (id, email, password)
VALUES (1, 'demo@tracabilite.ma', 'demo123')
ON DUPLICATE KEY UPDATE email=email;