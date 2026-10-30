# Stadium Management System Backend

**Student:** Mwebaze Geofrey  
**Student ID:** 26486  
**Project:** Stadium Management System  
**Course:** INSY 8322 Web Technology and Internet  
**Stack:** Java 17, Spring Boot, Spring Web, Spring Data JPA, PostgreSQL

## Run the API

1. Install Java 17 and PostgreSQL.
2. Create a PostgreSQL database named `stadium_db`.
3. Set the datasource URL, username, and password in `src/main/resources/application.properties` for your local PostgreSQL installation.
4. From the project root run:

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

5. The API is available at `http://localhost:8080`.

Hibernate creates or updates the tables on startup. SQL logging is enabled by `spring.jpa.show-sql=true`.

## Seed data

Use the API's own save endpoints to enter the records. Run the saves in this order so relationship IDs can be copied from each `/all` response:

1. POST four records to `/api/event-types/save`, with `name` values `Football`, `Concert`, `Athletics`, and `Ceremony`.
2. POST four records to `/api/zones/save`: zone numbers 1–4, named North Stand, South Stand, East Stand, and VIP Lounge. Leave zone 4 unassigned.
3. POST three records to `/api/venue-managers/save`, using names Alice Mutoni, Bob Nkusi, and Carol Uwase and assigning zones 1, 2, and 3 respectively. Copy the zone UUIDs from `/api/zones/all`. Use D3 to give Alice Football and Concert, Bob Athletics, and Carol Football. Leave Ceremony unused.
4. POST these five organizers to `/api/organizers/save`:

   | Organization | Contact person | Organizer code |
   |---|---|---|
   | Rayon Sports | Jean Bosco | ORG-0071 |
   | Rayon Sports | Marie Claire | ORG-0072 |
   | APR FC | Patrick Nziza | ORG-0073 |
   | Kigali Events | Diane Uwera | ORG-0074 |
   | Rwanda Music | Eric Habimana | ORG-0075 |

   Include `phone` and `email` strings in each body; these fields are part of the entity.
5. POST the following ten bookings to `/api/stadium-bookings/save`, replacing each organizer code and manager name with their UUIDs from `/api/organizers/all` and `/api/venue-managers/all`:

   | Date | Title | Organizer | Manager | Final status |
   |---|---|---|---|---|
   | 2026-10-30 | Rayon opener | ORG-0071 | Alice | DECLINED |
   | 2026-10-30 | Cup final | ORG-0072 | Alice | HELD |
   | 2026-10-31 | Kigali concert | ORG-0073 | Bob | HELD |
   | 2026-11-01 | Athletics meet | ORG-0071 | Carol | APPROVED |
   | 2026-11-02 | Ceremony gala | ORG-0074 | Alice | REQUESTED |
   | 2026-11-03 | Music festival | ORG-0075 | Bob | REQUESTED |
   | 2026-11-04 | Rayon home game | ORG-0071 | Carol | REQUESTED |
   | 2026-11-05 | Friendly match | ORG-0072 | Bob | REQUESTED |
   | 2026-11-06 | Sprint finals | ORG-0073 | Alice | REQUESTED |
   | 2026-11-07 | Year-end concert | ORG-0074 | Carol | REQUESTED |

   Every booking body needs `eventDate`, `eventTitle`, `expectedAttendance`, `organizer: {"id": "..."}`, and `venueManager: {"id": "..."}`. Save the Rayon opener first, move it to DECLINED, then save the Cup final on the same manager/date. Move the Cup final and Kigali concert through APPROVED to HELD, and move Athletics meet to APPROVED. These steps meet the manager conflict and status rules while preserving two HELD bookings.

Every newly saved booking starts as REQUESTED. Use `PATCH /api/stadium-bookings/{id}/status?status=...` to move it through the permitted status flow. D1 rejects dates before the current date, so the dates above are set in the future for 2026-10-29; move them forward if running the seed later. Decline the Rayon opener before saving the Cup final on the same manager/date so the conflict rule permits it. For a reproducible F2 check, choose Alice and 2026-11-02, which has a REQUESTED booking.

## API endpoints

| Requirement | Method and path |
|---|---|
| A2 | CRUD: `/api/event-types`, `/api/zones`, `/api/venue-managers`, `/api/organizers`, `/api/stadium-bookings` (`/save`, `/all`, `/{id}`, `/update/{id}`, `/delete/{id}`) |
| B1 | `GET /api/organizers/by-organization-name?organizationName=rayon%20sports` |
| B2 | `GET /api/stadium-bookings/by-status?status=REQUESTED` |
| B3 | `GET /api/stadium-bookings/between?start=2026-10-01&end=2026-10-31` |
| C1 | `GET /api/venue-managers/by-event-type?name=football` |
| C2 | `GET /api/zones/free` |
| C3 | `GET /api/event-types/unused` |
| C4 | `GET /api/organizers/of-venue-manager/{venueManagerId}` |
| D1 | `POST /api/stadium-bookings/save` |
| D2 | `PATCH /api/stadium-bookings/{id}/status?status=APPROVED` |
| D3 | `PUT /api/venue-managers/{venueManagerId}/event-types/{eventTypeId}` |
| D4 | `PATCH /api/venue-managers/{venueManagerId}/zone?zoneNumber=4` |
| E1 | `POST /api/ticket-sales/save?stadiumBookingId={id}` |
| E2 | `GET /api/ticket-sales/organizer/{organizerCode}` |
| E3 | `DELETE /api/ticket-sales/delete/{id}` and protected stadium-booking deletion |
| F1 | `GET /api/stadium-bookings/stats/by-status` |
| F2 | `PATCH /api/stadium-bookings/cancel-day?venueManagerId={id}&date=2026-11-02` |
| Bonus | `GET /api/dashboard/summary` |
| Bonus | `GET /api/stadium-bookings/page?page=0&size=5&sort=eventDate,desc` |

Import `stadium-management-system-midterm.postman_collection.json` into Postman. Set its collection variables (`baseUrl`, `venueManagerId`, `eventTypeId`, `organizerCode`, `bookingId`, `heldBookingId`, and `ticketSaleId`) to values from your seeded database before running the related requests. Set `cancelDate` to a date containing bookings for the chosen manager before running F2; with the seed data above, use Alice and `2026-11-02`. The A2 endpoint family contains separate CRUD operations for each of the five required entities.
