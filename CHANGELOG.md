# Changelog

Todo cambio notable a este proyecto será documentado aquí.  
Formato: Keep a Changelog  
Versionado: SemVer

---

## [Unreleased]
_(Sin cambios por ahora)_

---

## [0.4.0] – 2025-11-23
Implementación completa del Modo Especial y mejoras en modos de juego.

### Added
- (feature): Modo de juego Especial finalizado versión 2.0, con rebote en paredes (`cc9c354` – 2025-11-23)
- (feature): Modo Especial versión 1.0 (`d1cec2c` – 2025-11-22)
- (feature): Base inicial del sistema de habilidades especiales (`6b43190` – 2025-11-21)

### Fixed
- (fix): Unificación del sistema de goles para futuros modos (`6de2410` – 2025-11-21)

---

## [0.3.0] – 2025-10-30
Gran refactor de menús, modos de juego y estructura general.

### Added
- (feature/modos-de-juego): Estructura para añadir modos y agregado el modo práctica (`e1ec3be` – 2025-10-23)

### Changed
- (refactor/menu): Renderizado del menú extraído a clase gestora (`9a7d18c` – 2025-10-30)
- (refactor/menu): Gestor de entrada del menú extraído a clase independiente (`d668cd1` – 2025-10-30)
- (refactor/menus): Reestructuración completa del sistema de menús (`4edb56c` – 2025-10-23)
- (refactor/modos-de-juego): Gestión de jugadores y configuraciones migrada a ArrayList  
  (`e16a089`, `16f1adc` – 2025-10-29)
- (refactor): Refactor de clase Carga y núcleo del juego (`adf79e8` – 2025-10-22)
- (task): Estructura de clases para futuros modos (`9b0b2ee` – 2025-10-13)
- (merge): Unificación de varias ramas en una base funcional (`b525539` – 2025-10-13)

### Fixed
- (fix): Corrección en el funcionamiento de la barra de stamina (`6cd538b` – 2025-10-27)

---

## [0.2.0] – 2025-09-10
Primer sistema de goles, arcos y reorganización de subsistemas.

### Added
- (feat): Agregado de arcos y sistema de gol (`d2cb1b4` – 2025-10-07)
- (wip): Arco derecho y lógica de partido (`8ba326c` – 2025-09-10)
- (wip): Hitbox del arco izquierdo (`42a68ce` – 2025-09-10)

### Changed
- (refactor): Separación de física, entrada y render en sistemas (`79bd9f1` – 2025-09-10)
- (refactor): Unificación del sistema de jugadores con stamina, HUD y físicas (`45d2286` – 2025-09-08)
- (merge): Integración de rama de stamina (`5eafc82` – 2025-09-08)
- (feature): Modo 2 jugadores (`17b16e8` – 2025-08-26)
- (wip): Barra de stamina sigue al personaje (`ba28288` – 2025-08-14)
- (wip): HUD de stamina agregado (`c4c97cc` – 2025-08-14)
- (feat): Mejora de legibilidad en Personaje + HUDPersonaje (`fb81b89` – 2025-08-13)
- (wip): Test de barra de stamina (`e8b5ec7` – 2025-08-13)

### Docs
- Actualizaciones del CHANGELOG y README (`bea8289` – 2025-08-13)

---

## [0.1.1] – 2025-07-29
Sistema de animaciones y mejoras iniciales.

### Added
- Animaciones completas, pelota animada y patada (`79acf73` – 2025-07-29)
- Animaciones verticales (`0529db9` – 2025-07-22)
- Fondo e interacción básica del jugador (`47b6e4c` – 2025-07-21)

### Changed
- Reorganización del código de jugador y mapa (`7b05dd5` – 2025-07-21)

### Chore
- Inicio de rama develop (`4b3770f` – 2025-07-21)

### Fixed
- Corrección de README duplicado y estructura de archivos (`eaf0bea` – 2025-07-02)

---

## [0.1.0] – 2025-07-25
Versión inicial del proyecto.

### Added
- Creación del proyecto LIBGDX (`0031647` – 2025-05-17)
- Agregado CHANGELOG (`46d217b` – 2025-05-17)
- Primer README y sucesivas actualizaciones  
  (`b124f69`, `b7192bd`, `0ea9a8e`, `a08ef28`, `1da2c52`, `f2d406d`,  
  `f10bb59`, `1660455`, `c10e999`, `f4132b1`, `7da8b8e`)

### Removed
- Eliminación del directorio anterior del proyecto (`eca99ae` – 2025-05-17)

### Misc
- Archivos subidos manualmente (`2f1054e` – 2025-05-17)
- Merge inicial a develop (`29de754` – 2025-07-30)
