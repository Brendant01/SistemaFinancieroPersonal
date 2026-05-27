# Sistema Financiero Personal

Aplicación de escritorio desarrollada en Java que permite llevar un control claro y sencillo de las finanzas personales, calculando ingresos, gastos, ahorro y saldo libre de forma automática.

---

## Funciones

- **Registro de ingresos** — Salario principal y otros ingresos adicionales
- **Gastos fijos** — Electricidad y agua
- **Gastos personalizados** — Agrega y elimina gastos con nombre y monto
- **Cálculo de ahorro** — Dos modos disponibles:
  - *Sobre salario:* el porcentaje se calcula sobre el salario base
  - *Sobre restante:* el porcentaje se calcula sobre lo que queda después de gastos
- **Detección de déficit** — El sistema avisa si los gastos superan los ingresos
- **Reporte en PDF** — Genera automáticamente un archivo `Finanzas.pdf` con el resumen financiero del mes

---

## 🖥️ Requisitos

- Java 21 o superior → [Descargar Java](https://www.oracle.com/java/technologies/downloads/)

---

## Cómo ejecutar

**Opción 1 — Ejecutable (.exe):**
Descarga `SistemaFinanciero.exe` y ejecútalo directamente. Requiere tener Java 21 instalado.

**Opción 2 — JAR:**
Dentro de una terminal ejecuta:
java -jar SistemaFinancieroPersonal.jar

---

## Tecnologías utilizadas

- **Java 21** — Lenguaje principal
- **Java Swing** — Interfaz gráfica
- **iText 9** — Generación de PDF
- **Maven** — Gestión de dependencias

---

## Próximas mejoras

- [ ] Preguntar al usuario si desea generar el reporte PDF antes de crearlo
- [ ] Cambiar el porcentaje de ahorro a un selector con valores fijos (5%, 10%, 15%, 20%, 25%)
- [ ] Mejorar el diseño y contenido del reporte PDF
- [ ] Más funciones por definir

---

## Autor

**Victor José España Ramirez**
[GitHub](https://github.com/Brendant01)
