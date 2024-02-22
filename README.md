# Alarm Service Using DDD & Clean Architecture
Implementation of a spring web service that combines the domain driven design structure with clean architecture principles:
- Level 1: Domain
- Level 2: Application (contains the main services that deal with the business logic and DTO and interfaces to interact with outer - infrastructure - layer)
- Level 3: Entity (web package), Infrastructure (contains repository and rest client - HttpClient)

By combining those 2 architectures
1) we skip the extra Interfaces and Interface Adapters layer of clean architecture.
2) we skip the difficult DDD terminology and design.

but we keep main concepts such:
1) domain driven.
2) separation of concerns.
3) layers that are fully decoupled.
4) adapters (incoming requests, repositories, outgoing requests clients) that are easily changed without affecting other layers.
5) and because of the previous, we achieve a solution that is scalable, maintainable, and extensible.
