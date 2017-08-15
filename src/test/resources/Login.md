# Gmail login
As a user, I want to be able to log in to gmail, so that I can see my emails

### [Example](- "valid login test")
* Given I am on the [gmail login page](- "initialiseLogin()")
* When I [type in valid login credentials](- "enterCredentials()")
* Then the resulting page should have the title [Gmail](- "?=checkContentOfPage()") 
