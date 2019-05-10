package ch.xwr.seicentobookit.business;

import java.util.Date;

import ch.xwr.seicentobookit.business.model.SolAlvValues;
import ch.xwr.seicentobookit.entities.Employee;
import ch.xwr.seicentobookit.entities.Salary;
import ch.xwr.seicentobookit.entities.SalaryBvgBaseLine;
import ch.xwr.seicentobookit.entities.SalaryCalculationLine;

public class CalculateSalary {
	private Salary dto;
	private Employee emp;
	private SalaryCalculationLine clcDto;
	private SalaryBvgBaseLine bvlDto;
	private CalculationHelper helper;

	public CalculateSalary(Salary dto, Employee emp) {
		this.dto = dto;
		this.emp = emp;
		helper = new CalculationHelper();
	}
	
	public Salary getDto() {
		return this.dto;
	}
	
    public void calculateSalary()
    {
    	if (dto == null) return;
        if (dto.getEmployee() == null) return;
        loadBaseData();

        double salary = dto.getSlrSalaryBase();
        double sourceTax = 0;
        if (dto.getSlrAmountSourceTax() != null) sourceTax = dto.getSlrAmountSourceTax();

        dto.setSlrAmountAhv(helper.computePercentage(dto.getSlrSalaryBase(), 100., dto.getSlrFactorAhv()));
        dto.setSlrAmountAlv(helper.computePercentage(dto.getSlrBaseAlv(), 100., dto.getSlrFactorAlv()));

        //BigDecimal bvgfact = new BigDecimal(1); //dummy
        dto.setSlrAmountBvg(helper.computePercentage(dto.getSlrBaseBvg().doubleValue(), 100., getBvgFactor(false)));
        dto.setSlrAmountBvgEmp(helper.computePercentage(dto.getSlrBaseBvg().doubleValue(), 100., getBvgFactor(true)));

        dto.setSlrAmountFak(helper.computePercentage(dto.getSlrSalaryBase(), 100, dto.getSlrFactorFak()));
        dto.setSlrAmountSol(helper.computePercentage(dto.getSlrBaseSol(), 100., dto.getSlrFactorSol()));
        dto.setSlrAmountAdminfees(helper.computePercentage((dto.getSlrAmountAhv() * 2.), 100., dto.getSlrFactorAdmin()));
        
        salary = salary - dto.getSlrAmountAhv() - dto.getSlrAmountAlv() - dto.getSlrAmountBvgEmp().doubleValue();
        salary = salary - dto.getSlrAmountSol() - sourceTax + dto.getSlrKidsAdditon() + dto.getSlrBirthAddon().doubleValue();
        
        dto.setSlrSalaryNet(helper.swissCommercialRound(new Double(salary)));            
    }

    private void loadBaseData() {
        if (dto.getSlrDate() == null) dto.setSlrDate(new Date());        
    	
    	clcDto = helper.getCalculationBase(dto, emp);
    	bvlDto = helper.getBvgBaseLine(dto, emp);
        setBaseSolAlv();
	}

	private Double getBvgFactor(boolean worker) {
        double bvgfact = 0;
        if (worker) {
        	bvgfact = this.bvlDto.getSbxWorker();
        } else {
        	bvgfact = this.bvlDto.getSbxCompany();        	
        }
        return new Double(bvgfact);
    }
    
    private void setBaseSolAlv()
    {
    	SolAlvValues values = helper.getBaseSolAlvValues(dto, emp, clcDto);
    	    	    	              
        //Solidaritätsbeitrag unter/ober Limit
        if (values.getTotalSalary() > clcDto.getSlxSldLowerBoundry()) {      	
        	dto.setSlrBaseSol(values.getSolValue());
        }

        //Koordinationslohn ALV        
        dto.setSlrBaseAlv(dto.getSlrSalaryBase());
        if (values.getTotalSalary() > clcDto.getSlxCoordinationAlv())
        {
        	dto.setSlrBaseAlv(values.getBaseAlv());
        }
    }
    
}
