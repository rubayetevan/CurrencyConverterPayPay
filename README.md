# Currency Converter
## :triangular_flag_on_post: Functional Requirements
The required data must be fetched from the open exchange rates service.
> - See the documentation for information on how to use their API.
> - You must use a free account - not a paid one.
NOTE: Until Tuesday 24 May 2022, this code challenge required the use of the currencylayer service. If you started the code challenge on that day or before, it's okay to continue to use currencylayer. Otherwise, please note that the open exchange rates service must be used, and we will ask that any submissions using currencylayer be modified to use open exchange rates.
> 

- The required data must be persisted locally to permit the application to be used offline after data has been fetched.
- In order to limit bandwidth usage, the required data can be refreshed from the API no more frequently than once every 30 minutes.
- The user must be able to select a currency from a list of currencies provided by open exchange rates.
- The user must be able to enter the desired amount for the selected currency.
- The user must then be shown a list showing the desired amount in the selected currency converted into amounts in each currency provided by open exchange rates.
>
> - If exchange rates for the selected currency are not available via open exchange rates, perform the conversions on the app side.
> - When converting, floating point errors are acceptable.
>
**The project must contain unit tests that ensure correct operation.**
# :test_tube:Solution
## :bricks: Application Architecture
![Untitled-2022-08-13-1818](https://user-images.githubusercontent.com/16047748/184496385-00f20cae-34be-404b-b9cf-caf54eeef65a.png)
## :file_folder: Database Structure:
![Untitled-2022-08-13-1911](https://user-images.githubusercontent.com/16047748/184498647-e0d6ae9f-95d3-4c5c-b647-d2741cf8bb6a.png)
## :runner: Application process flow:
![app (2)](https://user-images.githubusercontent.com/16047748/184597344-81e4fe79-5a8d-45f6-8386-965c9f1793e4.png)

