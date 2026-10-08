-- Datos de ejemplo para lotes-trazabilidad-service
-- Categorías
INSERT INTO categorias_medicamento (nombre, descripcion, creado_por, creado_en, modificado_por, modificado_en) VALUES
('Analgésicos', 'Alivio del dolor', 'system', NOW(), 'system', NOW()),
('Antibióticos', 'Infecciones bacterianas', 'system', NOW(), 'system', NOW()),
('Antiinflamatorios', 'Reducción de inflamación', 'system', NOW(), 'system', NOW());

-- Proveedores
INSERT INTO proveedores (ruc, razon_social, telefono, email, direccion, estado, creado_por, creado_en, modificado_por, modificado_en) VALUES
('20123456789', 'Distribuidora Farmacéutica S.A.C.', '987654321', 'ventas@distribuidora.pe', 'Av. Principal 123', 'ACTIVO', 'system', NOW(), 'system', NOW()),
('20234567890', 'Proveedora Salud Total E.I.R.L.', '987123456', 'contacto@saludtotal.pe', 'Jr. Comercio 456', 'ACTIVO', 'system', NOW(), 'system', NOW());

-- Medicamentos
INSERT INTO medicamentos (codigo, nombre, descripcion, presentacion, concentracion, precio_venta, stock, stock_minimo, estado, categoria_id, creado_por, creado_en, modificado_por, modificado_en) VALUES
('MED-00001', 'Paracetamol', 'Analgésico y antipirético', 'Tableta', '500 mg', 2.50, 200, 20, 'ACTIVO', 1, 'system', NOW(), 'system', NOW()),
('MED-00002', 'Amoxicilina', 'Antibiótico', 'Cápsula', '500 mg', 8.90, 150, 15, 'ACTIVO', 2, 'system', NOW(), 'system', NOW()),
('MED-00003', 'Ibuprofeno', 'Antiinflamatorio', 'Tableta', '400 mg', 3.80, 180, 20, 'ACTIVO', 3, 'system', NOW(), 'system', NOW()),
('MED-00004', 'Diclofenaco', 'Antiinflamatorio', 'Tableta', '50 mg', 4.50, 120, 15, 'ACTIVO', 3, 'system', NOW(), 'system', NOW());

-- Lotes
INSERT INTO lotes_medicamento (numero_lote, medicamento_id, proveedor_id, cantidad, fecha_vencimiento, creado_por, creado_en, modificado_por, modificado_en) VALUES
('L2026-001', 1, 1, 100, DATE_ADD(CURDATE(), INTERVAL 12 MONTH), 'system', NOW(), 'system', NOW()),
('L2026-002', 1, 2, 100, DATE_ADD(CURDATE(), INTERVAL 18 MONTH), 'system', NOW(), 'system', NOW()),
('L2026-003', 2, 1, 150, DATE_ADD(CURDATE(), INTERVAL 24 MONTH), 'system', NOW(), 'system', NOW()),
('L2026-004', 3, 2, 180, DATE_ADD(CURDATE(), INTERVAL 9 MONTH), 'system', NOW(), 'system', NOW()),
('L2026-005', 4, 1, 120, DATE_ADD(CURDATE(), INTERVAL 15 MONTH), 'system', NOW(), 'system', NOW());
