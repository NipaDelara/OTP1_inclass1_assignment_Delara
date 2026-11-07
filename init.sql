-- Assignment 6: Database Localization
-- Approach: Translation Table
-- Database: MariaDB

-- Keep the existing temperature_unit table.
-- Add a separate table for localized unit names.

CREATE TABLE IF NOT EXISTS unit_translation (
    id INT AUTO_INCREMENT PRIMARY KEY,
    unit_id INT NOT NULL,
    lang VARCHAR(5) NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    UNIQUE KEY unique_unit_language (unit_id, lang),
    CONSTRAINT fk_unit_translation
        FOREIGN KEY (unit_id)
        REFERENCES temperature_unit(id)
) CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

-- Insert translations for Celsius, Fahrenheit and Kelvin.
-- The matching units must already exist in temperature_unit.

INSERT INTO unit_translation
    (unit_id, lang, name, description)
SELECT id, 'en', 'Celsius', 'Metric temperature unit'
FROM temperature_unit
WHERE symbol = '°C'
ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    description = VALUES(description);

INSERT INTO unit_translation
    (unit_id, lang, name, description)
SELECT id, 'fi', 'Celsius', 'Lämpötilan mittayksikkö'
FROM temperature_unit
WHERE symbol = '°C'
ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    description = VALUES(description);

INSERT INTO unit_translation
    (unit_id, lang, name, description)
SELECT id, 'sv', 'Celsius', 'Enhet för temperatur'
FROM temperature_unit
WHERE symbol = '°C'
ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    description = VALUES(description);

INSERT INTO unit_translation
    (unit_id, lang, name, description)
SELECT id, 'ja', '摂氏', '温度の単位'
FROM temperature_unit
WHERE symbol = '°C'
ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    description = VALUES(description);

INSERT INTO unit_translation
    (unit_id, lang, name, description)
SELECT id, 'ar', 'درجة مئوية', 'وحدة قياس درجة الحرارة'
FROM temperature_unit
WHERE symbol = '°C'
ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    description = VALUES(description);
