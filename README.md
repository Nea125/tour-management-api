SQL Logical Execution Order
1. FROM / JOIN
   Get data from tables and join related tables.
2. WHERE
   Filter individual rows before grouping.
   Example: only include PAID bookings.
3. GROUP BY
   Group rows that share the same value.
   Example: group bookings by destination.
4. HAVING
   Filter groups after grouping and aggregation.
   Example: only destinations with more than 5 bookings.
5. SELECT
   Choose columns and calculate values using aggregate functions such as COUNT(), SUM(), and AVG().
6. ORDER BY
   Sort the final results.
   Example: sort destinations by booking count in descending order.
7. LIMIT
   Restrict the number of rows returned.
   Example: return only the top 10 destinations.

. NOTE:
 Queries executed in order: FROM → WHERE → GROUP BY → HAVING → SELECT → ORDER BY → LIMIT

 GROUP BY = group the rows.
 COUNT(), SUM(), AVG(), MAX(), MIN() = aggregate functions.
 HAVING = filter groups based on aggregate results.