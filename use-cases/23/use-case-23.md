# USE CASE: 23 Produce a Report on the Top N Populated Cities in a Region where N is Provided by the User.

## CHARACTERISTIC INFORMATION

### Goal in Context

As an *organisation that requires reporting on population information.*
I want *a report of the top N populated cities in a region where N is given.*
So that *we have easy access to population information.*

### Scope

Organisation.

### Level

Primary task.

### Preconditions

Database contains world data.

### Success End Condition

A report is available for the organisation containing the top N populated cities in a region where N is given.

### Failed End Condition

No report is produced.

### Primary Actor

A member of the organisation.

### Trigger

The member of the organisation requires information of the top N populated cities in a region where
N is given.

## MAIN SUCCESS SCENARIO

1. The member of the organisation requires access to this population information of the top N populated
   cities in a region.
2. The member of the organisation provides N as the given input.
3. The member of the organisation extracts a report of the top N populated cities in a region where N is given.
4. The member of the organisation now has the information available to them in a report.

## EXTENSIONS

3. **N is greater than the number of cities in a region**
    1. The report will contain all the cities in a region as long as that count is less than N.

## SUB-VARIATIONS

None.

## SCHEDULE

**DUE DATE**: Release 1.0