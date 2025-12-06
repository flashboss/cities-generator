---
layout: guide
title: Cities Generator
permalink: /guide/
---

# Cities Generator

A comprehensive solution for generating and using cities data. This project consists of three main components:

- **Library**: Java library for generating cities descriptor files
- **Service**: REST service for accessing cities data via API
- **Frontend**: React component for displaying cities in web applications

## Library

Java library for generating descriptor files with cities data. Supports multiple countries, languages, and data providers.

For detailed documentation, installation instructions, and usage examples, see the [Library README](library/README.md).

## Service

REST service for accessing cities data. Provides a REST API that returns JSON format with the cities of the used country according to the configured parameters.

For detailed documentation, installation instructions, Docker setup, and configuration examples, see the [Service README](service/README.md).

## Generated Data

Samples of generated cities can be found online:

- <https://raw.githubusercontent.com/flashboss/cities-generator/master/_db>

The structure is {continent}/{country}/{language}.json (e.g., EU/IT/it.json, EU/GB/en.json)

## Frontend

A portable, hierarchical dropdown menu component for displaying cities data from cities-generator JSON files. The component is designed to work in any platform (WordPress, Drupal, Liferay, Joomla, etc.) with minimal integration effort.

For detailed documentation, installation instructions, and usage examples, see the [Frontend README](frontend/README.md).
